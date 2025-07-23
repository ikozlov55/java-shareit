package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>, ItemRequestRepositoryExtension {
    List<ItemRequest> findByUserId(long userId);

    List<ItemRequest> findByUserIdNot(long userId);
}
