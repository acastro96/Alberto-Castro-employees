# Sirma Employee Collaboration - Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 20.1.1.

This is the frontend application for the **Sirma Employee Collaboration Test**.  
It allows the user to upload a CSV file, parse and validate the input data, and display the result of employee collaborations based on common projects.

## Features

- Upload CSV file from the local file system
- Parse CSV and display it in a responsive Material table with pagination
- Automatically validates date formats and structure before sending to the backend
- Supports multiple date formats (e.g. `yyyy-MM-dd`, `dd/MM/yyyy`, `MM/dd/yyyy`, `dd-MMM-yyyy`, `MMM d, yyyy`)
- Handles `NULL` as a valid value for `dateTo` (equivalent to current date)
- Sends valid data to the backend and displays the result in a separate Material table
- Displays error messages for malformed input or failed backend responses
- Fully responsive layout and mobile-friendly

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Development notes

- provideHttpClient() is configured in app.config.ts

- The fetch option is enabled to support SSR and better HTTP performance

- All logic is handled in a single standalone component (file-upload)

- Error handling and parsing are done client-side to ensure a clean contract with the backend

- Data is normalized before being sent to the backend (dates are sent in yyyy-MM-dd format)

## CSV Format Guidelines
The expected CSV structure is:
```csv
empId,projectId,dateFrom,dateTo
1001,300,2020-01-01,2020-06-01
1002,300,2020-02-01,2020-06-01
```

- `empId`: numeric
- `projectId`: numeric
- `dateFrom` and `dateTo`: can be in any supported format
- `dateTo` may be `NULL` (interpreted as today)

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.