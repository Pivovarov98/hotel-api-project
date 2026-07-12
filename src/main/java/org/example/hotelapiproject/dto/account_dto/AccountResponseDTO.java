package org.example.hotelapiproject.dto.account_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hotelapiproject.dto.hotel_dto.HotelShortDTO;
import org.example.hotelapiproject.dto.review_dto.ReviewShortResponseDTO;
import org.example.hotelapiproject.entity.Hotel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDTO {

    private String email;
    private String name;
    private String surname;
    private List<HotelShortDTO> hotels;
    private List<ReviewShortResponseDTO> reviews;
}
