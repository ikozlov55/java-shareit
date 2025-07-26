package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;


public class ItemRepositoryExtensionImpl implements ItemRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Item findByIdOrThrow(long itemId) {
        return entityManager.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", itemId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Item %d not found!", itemId)));
    }
}
