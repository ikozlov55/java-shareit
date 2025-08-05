package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestRepositoryExtensionImpl implements ItemRequestRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ItemRequest findByIdOrThrow(long requestId) {
        return entityManager.createQuery("SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", requestId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Item request %d not found!", requestId)));
    }

}
