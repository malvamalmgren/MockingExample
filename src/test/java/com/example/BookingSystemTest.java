package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingSystemTest {

    @InjectMocks
    private BookingSystem bookingSystem;
    @Mock
    private Room room;
    @Mock
    private NotificationService notificationService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private TimeProvider timeProvider;

    //bookRoom
    @Nested
    @DisplayName("Tests for bookRoom")
    class BookRoomTests {
        @DisplayName("Should throw exception for invalid arguments")
        @ParameterizedTest
        @MethodSource("invalidArguments")
        void shouldThrowExceptionForInvalidArguments(String roomId, LocalDateTime startTime, LocalDateTime endTime, String expectedMessage) {
            assertThatThrownBy(() -> bookingSystem.bookRoom(roomId, startTime, endTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(expectedMessage);
        }

        static Stream<Arguments> invalidArguments() {
            LocalDateTime now = LocalDateTime.of(2025, 3, 3, 10, 0);
            String message = "Bokning kräver giltiga start- och sluttider samt rum-id";
            return Stream.of(
                    Arguments.of(null, now.plusHours(1), now.plusHours(2), message),
                    Arguments.of("room1", null, now.plusHours(2), message),
                    Arguments.of("room1", now.plusHours(1), null, message));
        }
    /*
    @Test
    @DisplayName("Should throw IllegalArgumentException when roomId is null")
    void shouldThrowIllegalArgumentExceptionWhenRoomIdIsNull() {
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 3, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 3, 14, 0);

        assertThatThrownBy(() -> bookingSystem.bookRoom(null, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when startTime is null")
    void shouldThrowIllegalArgumentExceptionWhenStartTimeIsNull() {
        LocalDateTime startTime = null;
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 3, 12, 0);

        assertThatThrownBy(() -> bookingSystem.bookRoom("1", startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when endTime is null")
    void shouldThrowIllegalArgumentExceptionWhenEndTimeIsNull() {
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 3, 12, 0);
        LocalDateTime endTime = null;

        assertThatThrownBy(() -> bookingSystem.bookRoom("1", startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bokning kräver giltiga start- och sluttider samt rum-id");
    }
    */

        @Test
        @DisplayName("Should throw IllegalArgumentException if given past startTime")
        void shouldThrowIllegalArgumentExceptionIfGivenPastStartTime() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            LocalDateTime startTime = timeProvider.getCurrentTime().minusDays(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

            assertThatThrownBy(() -> bookingSystem.bookRoom("room1", startTime, endTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Kan inte boka tid i dåtid");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when endTime is before startTime")
        void shouldThrowIllegalArgumentExceptionWhenEndTimeIsBeforeStartTime() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(4);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

            assertThatThrownBy(() -> bookingSystem.bookRoom("room1", startTime, endTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Sluttid måste vara efter starttid");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if room does not exist")
        void shouldThrowIllegalArgumentExceptionIfRoomDoesNotExist() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

            assertThatThrownBy(() -> bookingSystem.bookRoom("room1", startTime, endTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Rummet existerar inte");
        }

        @Test
        @DisplayName("Should not throw exception if room exists")
        void shouldNotThrowExceptionIfRoomExists() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            when(roomRepository.findById("room1")).thenReturn(Optional.of(room));
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);
            // Make sure room *is* available
            when(room.isAvailable(startTime, endTime)).thenReturn(true);

            assertThat(bookingSystem.bookRoom("room1", startTime, endTime))
                    .isTrue();
        }

        @Test
        @DisplayName("Should return false if room is not available")
        void shouldReturnFalseIfRoomIsNotAvailable() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            when(roomRepository.findById("room1")).thenReturn(Optional.of(room));
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

            // Make sure room is not available for those times
            when(room.isAvailable(startTime, endTime)).thenReturn(false);

            assertThat(bookingSystem.bookRoom("room1", startTime, endTime))
                    .isFalse();
        }

        @Test
        @DisplayName("Should return true if room is available")
        void shouldReturnTrueIfRoomIsAvailable() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            when(roomRepository.findById("room1")).thenReturn(Optional.of(room));
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);
            when(room.isAvailable(startTime, endTime)).thenReturn(true);

            assertThat(bookingSystem.bookRoom("room1", startTime, endTime))
                    .isTrue();
        }
    }

    //________________________________________________________________________________________________

    //getAvailableRooms
    @Nested
    @DisplayName("Tests for getAvailableRooms")
    class GetAvailableRoomsTests {

        @DisplayName("Should throw exception for invalid getAvailableRooms times")
        @ParameterizedTest
        @MethodSource("invalidTimesForGetAvailableRooms")
        void shouldThrowExceptionForInvalidGetAvailableRoomsTimes(LocalDateTime startTime, LocalDateTime endTime, String expectedMessage) {
            assertThatThrownBy(() -> bookingSystem.getAvailableRooms(startTime, endTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(expectedMessage);
        }

        private static Stream<Arguments> invalidTimesForGetAvailableRooms() {
            LocalDateTime now = LocalDateTime.of(2025, 3, 3, 10, 0);
            return Stream.of(
                    Arguments.of(null, now.plusHours(2), "Måste ange både start- och sluttid"),
                    Arguments.of(now.plusHours(1), null, "Måste ange både start- och sluttid"),
                    Arguments.of(now.plusHours(2), now.plusHours(1), "Sluttid måste vara efter starttid")
            );
        }

    /*
    @Test
    @DisplayName("Should throw IllegalArgumentException if startTime is null")
    void shouldThrowIllegalArgumentExceptionIfStartTimeIsNull() {
        when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
        LocalDateTime startTime = null;
        LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

        assertThatThrownBy(() -> bookingSystem.getAvailableRooms(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if endTime is null")
    void shouldThrowIllegalArgumentExceptionIfEndTimeIsNull() {
        when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
        LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
        LocalDateTime endTime = null;

        assertThatThrownBy(() -> bookingSystem.getAvailableRooms(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if endTime is before startTime")
    void shouldThrowIllegalArgumentExceptionIfEndTimeIsBeforeStartTime() {
        when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
        LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(3);
        LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

        assertThatThrownBy(() -> bookingSystem.getAvailableRooms(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sluttid måste vara efter starttid");
    }
     */

        @Test
        @DisplayName("Should return available rooms")
        void shouldReturnAvailableRooms() {
            when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
            LocalDateTime startTime = timeProvider.getCurrentTime().plusHours(1);
            LocalDateTime endTime = timeProvider.getCurrentTime().plusHours(2);

            // Create two mock rooms, one available, one not
            Room roomA = Mockito.mock(Room.class);
            Room roomB = Mockito.mock(Room.class);
            when(roomA.isAvailable(startTime, endTime)).thenReturn(true);
            when(roomB.isAvailable(startTime, endTime)).thenReturn(false);

            List<Room> allRooms = List.of(roomA, roomB);
            when(roomRepository.findAll()).thenReturn(allRooms);

            List<Room> available = bookingSystem.getAvailableRooms(startTime, endTime);

            assertThat(bookingSystem.getAvailableRooms(startTime, endTime).contains(roomA))
                    .isTrue();
        }
    }

}
