package com.azry.library_management_system.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReturnEvent {
    private Long userId;
    private Long bookId;
}