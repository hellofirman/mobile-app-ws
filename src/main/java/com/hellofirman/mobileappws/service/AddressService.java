package com.hellofirman.mobileappws.service;

import java.util.List;

import com.hellofirman.mobileappws.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);
	AddressDto getAddress(String addressId);
}
