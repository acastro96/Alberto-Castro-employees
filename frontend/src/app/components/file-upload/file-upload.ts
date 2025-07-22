import { CommonModule } from '@angular/common';
import { Component, ChangeDetectorRef, AfterViewInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as Papa from 'papaparse';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { parse, isValid, format } from 'date-fns';


@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule],
  templateUrl: './file-upload.html',
  styleUrl: './file-upload.css'
})
export class FileUpload implements AfterViewInit {
  selectedFile: File | null = null;
  parsedData: any[] = [];
  result: any = null;
  error: string | null = null;

  csvDataSource = new MatTableDataSource<any>();
  csvDisplayedColumns: string[] = ['empId', 'projectId', 'dateFrom', 'dateTo'];

  resultDataSource = new MatTableDataSource<any>();
  resultColumns: string[] = ['emp1Id', 'emp2Id', 'commonProjects', 'totalDays'];

  acceptedFormats = [
    'yyyy-MM-dd',
    'dd/MM/yyyy',
    'MM-dd-yyyy',
    'yyyy/MM/dd',
    'dd-MM-yyyy',
    'MMM d, yyyy',   
    'd MMM yyyy',     
    'MM/dd/yyyy'
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  

  ngAfterViewInit(): void {
    this.csvDataSource.paginator = this.paginator;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.selectedFile = input.files[0];
      this.readAndParseFile();
    }
  }

  private readAndParseFile(): void {
    if (!this.selectedFile) return;

    const reader = new FileReader();
    reader.onload = () => {
      const csvData = reader.result as string;
      Papa.parse(csvData, {
        header: true,
        skipEmptyLines: true,
        complete: (result) => {

          const rawData = result.data as any[];
          const validatedData = [];
          for (const row of rawData) {
            const normDateFrom = this.parseDateToStandard(row.dateFrom);
            let normDateTo: string | null;
            if (!row.dateTo || row.dateTo.trim().toLowerCase() === 'null') {
              normDateTo = "NULL";
            } else {
              normDateTo = this.parseDateToStandard(row.dateTo);
            }

            if (!normDateFrom || !normDateTo) {
              this.error = `Invalid date format in row: ${JSON.stringify(row)}`;
              this.parsedData = [];
              this.cdr.markForCheck();
              return;
            }

            validatedData.push({
              ...row,
              dateFrom: normDateFrom,
              dateTo: normDateTo
            });
          }

          this.parsedData = validatedData;
          this.csvDataSource.data = this.parsedData;
          this.error = null;
          this.cdr.markForCheck();
          console.log('CSV parsed:', this.parsedData);

          if (!this.parsedData || this.parsedData.length === 0) {
            this.error = 'CSV file contains no valid data.';
          }
        },
        error: () => {
          this.error = 'Error parsing CSV file';
          this.cdr.markForCheck();
        }
      });
    };

    reader.readAsText(this.selectedFile);
  }

  upload(): void {
    if (!this.parsedData || this.parsedData.length === 0) {
      this.error = 'No data to upload. Please load a valid CSV first.';
      return;
    }

    this.http.post<any>('http://localhost:8080/api/employee/calculate-time-together', this.parsedData)
      .subscribe({
        next: (res) => {
          this.result = res;
          this.resultDataSource.data = res;
          this.error = null;
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.result = null;
          this.error = err?.error?.message || 'Error processing request';
          this.cdr.markForCheck();
        }
      });
  }

  parseDateToStandard(dateStr: string): string | null {
    for (const fmt of this.acceptedFormats) {
      const parsedDate = parse(dateStr, fmt, new Date());
      if (isValid(parsedDate)) {
        return format(parsedDate, 'yyyy-MM-dd');
      }
    }
    return null; // no válido
  }

}
