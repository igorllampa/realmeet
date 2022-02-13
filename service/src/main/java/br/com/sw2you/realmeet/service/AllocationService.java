package br.com.sw2you.realmeet.service;

import static br.com.sw2you.realmeet.util.DateUtils.*;
import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.*;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2you.realmeet.exception.AllocationCannotBeUpdatedException;
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
        var allocation = getAllocationOrThrow(allocationId.longValue());

        if(isAllocationInThePast(allocation)){
            throw new AllocationCannotBeDeletedException();
        }

        allocationRepository.delete(allocation);
    }

    @Transactional
    public void updateAllocation(Long allocationId, UpdateAllocationDTO updateAllocationDTO){
        var allocation = getAllocationOrThrow(allocationId);

        if(isAllocationInThePast(allocation)){
            throw new AllocationCannotBeUpdatedException();
        }

        allocationValidator.validate(allocationId, updateAllocationDTO);

        allocationRepository.updateAllocation(
                allocationId,
                updateAllocationDTO.getSubject(),
                updateAllocationDTO.getStartAt(),
                updateAllocationDTO.getEndAt()
        );
    }

    private Allocation getAllocationOrThrow(Long allocationId) {
        return allocationRepository
            .findById(allocationId)
            .orElseThrow(() -> new AllocationNotFoundException("Allocation not found: " + allocationId));
    }

    private boolean isAllocationInThePast(Allocation allocation){
        return allocation.getEndAt().isBefore(now());
    }
}
