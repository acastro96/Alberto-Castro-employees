import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FileUpload } from './components/file-upload/file-upload';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, FileUpload],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('front-sirma');
}
