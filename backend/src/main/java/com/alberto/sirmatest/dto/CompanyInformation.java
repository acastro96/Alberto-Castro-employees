package com.alberto.sirmatest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Information about employee's project involvement")
public record CompanyInformation(

    @Schema(description = "Employee ID", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Employee ID cannot be null")
    Integer empId,

    @Schema(description = "Project ID", example = "456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Project ID cannot be null")
    Integer projectId,

    @Schema(description = "Start date in YYYY-MM-DD", example = "2023-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Start date cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "DateFrom must be in format YYYY-MM-DD")
    String dateFrom,

    @Schema(description = "End date in YYYY-MM-DD or NULL", example = "2023-04-15")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$|^NULL$", message = "DateTo must be in format YYYY-MM-DD or NULL")
    String dateTo
){ }
