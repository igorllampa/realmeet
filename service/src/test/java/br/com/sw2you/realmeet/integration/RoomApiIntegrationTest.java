package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.utils.TestConstants.TEST_CLIENT_API_KEY;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateRoomDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.RoomApi;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;


class RoomApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoomApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testGetRoomSucess(){

        var room = newRoomBuilder().build();
        roomRepository.saveAndFlush(room);

        assertNotNull(room.getId());
        assertTrue(room.getActive());

        var dto = api.getRoom(TEST_CLIENT_API_KEY, room.getId());

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testGetRoomInactive(){
        var room = newRoomBuilder().active(false).build();
        roomRepository.saveAndFlush(room);

        assertFalse(room.getActive());

        assertThrows(HttpClientErrorException.NotFound.class, () -> api.getRoom(TEST_CLIENT_API_KEY, room.getId()) );
    }

    @Test
    void testGetRoomDoesNotExist(){
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.getRoom(TEST_CLIENT_API_KEY, DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess(){
        var createRoomDto = newCreateRoomDTO();
        var roomDto = api.createRoom(TEST_CLIENT_API_KEY, createRoomDto);

        assertEquals(createRoomDto.getName(), roomDto.getName());
        assertEquals(createRoomDto.getSeats(), roomDto.getSeats());
        assertNotNull(roomDto.getId());

        var room = roomRepository.findById(roomDto.getId()).orElseThrow();

        assertEquals(roomDto.getName(), room.getName());
        assertEquals(roomDto.getSeats(), room.getSeats());
    }

    @Test
    void testCreateRoomValidationError(){
        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.createRoom(TEST_CLIENT_API_KEY, (CreateRoomDTO) newCreateRoomDTO().name(null))
        );
    }

    @Test
    void testDeleteRoomSuccess(){
        var roomId = roomRepository.saveAndFlush(newRoomBuilder().build()).getId();
        api.deleteRoom(TEST_CLIENT_API_KEY, roomId);

        assertFalse(roomRepository.findById(roomId).orElseThrow().getActive());
    }

    @Test
    void testDeleteRoomDoesNotExist(){
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteRoom(TEST_CLIENT_API_KEY, 1L));
    }

    @Test
    void testUpdateRoomSuccess(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var updateRoomDTO = new UpdateRoomDTO().name(room.getName() + "_").seats(room.getSeats() + 1);

        api.updateRoom(TEST_CLIENT_API_KEY, room.getId(), updateRoomDTO);

        var updatedRoom = roomRepository.findById(room.getId()).orElseThrow();

        assertEquals(updatedRoom.getName(), updateRoomDTO.getName());
        assertEquals(updatedRoom.getSeats(), updateRoomDTO.getSeats());
    }

    @Test
    void testUpdateRoomDoesNotExist(){
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.updateRoom(TEST_CLIENT_API_KEY, 1L, new UpdateRoomDTO().name("Room").seats(2))
        );
    }

    @Test
    void testUpdateRoomValidationError(){
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());

        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.updateRoom(TEST_CLIENT_API_KEY, room.getId(), new UpdateRoomDTO().name(null).seats(2))
        );
    }
}
