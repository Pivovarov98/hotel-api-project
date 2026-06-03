package org.example.hotelapiproject.dto.account_dto;

import lombok.Data;
import org.example.hotelapiproject.dto.hotel_dto.HotelShortDTO;
import org.example.hotelapiproject.entity.Hotel;

import java.util.List;

@Data
public class AccountResponseDTO {

    private String email;
    private String name;
    private String surname;
    private List<HotelShortDTO> hotels;
}
