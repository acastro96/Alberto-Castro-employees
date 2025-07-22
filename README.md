# Sirma Test - Alberto Castro

This repository contains the solution for the **Sirma Employee Collaboration Task**. The goal is to process a CSV file containing employee participation in projects, and determine the pair of employees who have worked together for the longest total time across one or more projects.

---

## Repository Structure

Alberto-Castro-employees/

├── backend/ # Spring Boot backend (Java 21, Spring Boot 3.2.5)

├── frontend/ # Angular 20.1.1 standalone frontend

├── test-suite/ # Sample CSV test files for validating scenarios

└── README.md # General documentation (this file)


---

## Solution Overview

The application allows a user to:

1. Upload a CSV file containing employee-project-date information.
2. Validate that data client-side (including date formats and required columns).
3. Normalize the data and send it to the backend.
4. Process that data to calculate the employee pair that has worked the most days together.
5. Display the result in a responsive and styled data grid.

---

## Key Features

- Supports multiple date formats, including:
  - `yyyy-MM-dd`
  - `MM/dd/yyyy`
  - `dd-MM-yyyy`
  - `yyyy/MM/dd`
  - `MMM d, yyyy` (e.g., Jan 1, 2020)
- Handles `NULL` values in the `dateTo` field as today's date.
- Validates CSV structure and content before backend submission.
- Backend processes input concurrently using an `ExecutorService`.
- Fully styled Angular Material UI with pagination and responsiveness.
- Result grid includes:
  - Employee ID #1
  - Employee ID #2
  - Project(s) in common
  - Total days worked together

---

## Test Files

The `test-suite/` folder contains 6 CSV files covering:

- One pair with one project
- Two pairs with separate projects
- One pair with multiple projects
- Five different date formats
- Handling of NULL dateTo values

These can be used for manual testing or as sample inputs for automation.

---

## Running the Project

Follow setup instructions inside each subdirectory:

- [Backend Setup](./backend/README.md)
- [Frontend Setup](./frontend/README.md)

---

## 📬 Requirements Compliance

1. `dateTo` can be `NULL`, treated as today  
2. Input data must be uploaded from a CSV file  
3. Solution is hosted on GitHub with proper structure  
  ➡ Repository: `Alberto-Castro-employees`  
  ➡ Contains backend, frontend, and test-suite folders  
