package io.tanibilet.server.shared;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageableDto(
        @NotNull
        @Min(0)
        Integer pageNumber,

        @NotNull
        @Max(100)
        @Min(1)
        Integer pageSize
) {

    public Pageable toPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
