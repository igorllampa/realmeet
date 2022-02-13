package br.com.sw2you.realmeet.service;

import static br.com.sw2you.realmeet.util.DateUtils.*;
import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.*;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2you.realmeet.exception.AllocationNotFoundException;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.repository.AllocationRepository;
import br.com.sw2you.realmeet.repository.RoomRepository;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationService {

    private final RoomRepository roomRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationValidator allocationValidator;
    private final AllocationMapper allocationMapper;

    public AllocationService(
            RoomRepository roomRepository,
            AllocationRepository allocationRepository,
            AllocationValidator allocationValidator,
            AllocationMapper allocationMapper) {
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
        this.allocationValidator = allocationValidator;
        this.allocationMapper = allocationMapper;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository.findById(createAllocationDTO.getRoomId()).orElseThrow(() -> new RoomNotFoundException("Room not found: " + createAllocationDTO.getRoomId()));
        allocationValidator.validate(createAllocationDTO);

        var allocation = allocationMapper.fromCreateAllocationDTOToEntity(createAllocationDTO, room);
        allocationRepository.save(allocation);
        return allocationMapper.fromEntityToAllocationDTO(allocation);
    }

    public void deleteAllocation(Long allocationId){
        var allocation = allocationRepository
            .findById(allocationId)
            .orElseThrow(() -> new AllocationNotFoundException("Allocation not found: " + allocationId));

        if(allocation.getEndAt().isBefore(now())){
            throw new AllocationCannotBeDeletedException();
        }

        allocationRepository.delete(allocation);
    }
}
