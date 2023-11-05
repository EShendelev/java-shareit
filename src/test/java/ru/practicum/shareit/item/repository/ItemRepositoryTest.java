package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestItemRepository requestItemRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Item item3;
    RequestItem request1;
    RequestItem request2;
    RequestItem request3;


    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));

        request1 = requestItemRepository
                .save(new RequestItem(null, "testDescription1", user1, LocalDateTime.now()));
        request2 = requestItemRepository
                .save(new RequestItem(null, "testDescription2", user1, LocalDateTime.now()));
        request3 = requestItemRepository
                .save(new RequestItem(null, "testDescription3", user2, LocalDateTime.now()));

        item1 = itemRepository
                .save(new Item(null, "testName1", "testDescription1 wow",
                        true, user1, request1));
        item2 = itemRepository
                .save(new Item(null, "testName2", "testDescription2",
                        true, user2, request2));
        item3 = itemRepository
                .save(new Item(null, "testName3 wow", "testDescription3",
                        true, user2, request3));
    }

    @Test
    void searchTest() {
        List<Item> results = itemRepository.search("wow", PageRequest.of(0, 10));

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }

    @Test
    void searchTestWithNoItemsForKeyword() {
        List<Item> results = itemRepository.search("testy", PageRequest.of(0, 10));

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void findAllByOwnerIdTest() {
        List<Item> results = itemRepository.findAllByOwnerIdOrderById(user2.getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }

    @Test
    void findAllByRequestIdTest() {
        List<Item> results = itemRepository.findAllByRequestId(request1.getId());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findAllByRequestInTest() {
        List<Item> results = itemRepository.findAllByRequestIn(List.of(request1));

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

}

