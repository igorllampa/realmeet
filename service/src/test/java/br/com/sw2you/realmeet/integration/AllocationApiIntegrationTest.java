package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.utils.TestConstants.*;
import static br.com.sw2you.realmeet.utils.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.repository.AllocationRepository;
import br.com.sw2you.realmeet.repository.RoomRepository;
import br.com.sw2you.realmeet.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;


class AllocationApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateAllocationuccess(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().id(1L).build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO =  api.createAllocation(createAllocationDTO);

        assertNotNull(allocationDTO.getId());
        assertEquals(room.getId(), allocationDTO.getRoomId());
        assertEquals(createAllocationDTO.getSubject(), allocationDTO.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocationDTO.getEmployeeName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocationDTO.getEmployeeEmail());
        assertTrue(createAllocationDTO.getStartAt().isEqual(allocationDTO.getStartAt()));
        assertTrue(createAllocationDTO.getEndAt().isEqual(allocationDTO.getEndAt()));
    }

    @Test
    void testCreateAllocationValidationError(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDTO()
            .roomId(room.getId())
            .subject(null);

        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> api.createAllocation(createAllocationDTO)
        );
    }

    @Test
    void testCreateAllocationWhenRoomDoesNotExist(){

        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> api.createAllocation(newCreateAllocationDTO())
        );
    }

    @Test
    void testDeleteAllocationSuccess(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());

        api.deleteAllocation(allocation.getId());

        assertFalse(allocationRepository.findById(allocation.getId()).isPresent());
    }

    @Test
    void testDeleteAllocationInThePast(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(
            newAllocationBuilder(room)
                .startAt(DateUtils.now().minusDays(1))
                .endAt(DateUtils.now().minusDays(1).plusHours(1))
                .build());

        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.deleteAllocation(allocation.getId()));
    }

    @Test
    void testDeleteAllocationDoesNotExist(){
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteAllocation(1L));
    }

    @Test
    void testUpdateAllocationSuccess(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().id(1L).build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO =  api.createAllocation(createAllocationDTO);
        var updateAllocationDTO = newUpdateAllocationDTO()
            .subject(DEFAULT_ALLOCATION_SUBJECT + "_")
            .startAt(DEFAULT_ALLOCATION_START_AT.plusDays(1))
            .endAt(DEFAULT_ALLOCATION_END_AT.plusDays(1));

        api.updateAllocation(
            allocationDTO.getId(),
            updateAllocationDTO
        );

        var allocation = allocationRepository.findById(allocationDTO.getId()).orElseThrow();

        assertEquals(updateAllocationDTO.getSubject(), allocation.getSubject());
        assertTrue(updateAllocationDTO.getStartAt().isEqual(allocation.getStartAt()));
        assertTrue(updateAllocationDTO.getEndAt().isEqual(allocation.getEndAt()));

    }

    @Test
    void testUpdateAllocationDoesNotExist(){
        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> api.updateAllocation(1L, newUpdateAllocationDTO())
        );
    }

    @Test
    void testUpdateRoomValidationError(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().id(1L).build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO =  api.createAllocation(createAllocationDTO);

        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> api.updateAllocation(allocationDTO.getId(), newUpdateAllocationDTO().subject(null))
        );
    }
}
