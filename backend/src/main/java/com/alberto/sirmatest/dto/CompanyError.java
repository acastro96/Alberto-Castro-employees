package com.alberto.sirmatest.dto;

import java.time.LocalDateTime;

public record CompanyError(String message, String errorType, LocalDateTime timestamp){
}
