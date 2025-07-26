package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryExtension {
    List<Item> findByOwnerId(long userId);

    @Query("""
             SELECT i
               FROM Item i
              WHERE i.available is true
                AND (LOWER(i.name) LIKE %:text%
                 OR LOWER(i.description) LIKE %:text%)
            """)
    List<Item> itemsSearch(@Param("text") String text);
}
