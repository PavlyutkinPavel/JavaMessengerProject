package com.messenger.myperfectmessenger.repository;
import com.messenger.myperfectmessenger.domain.FriendsList;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class FriendsListRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<FriendsList> findById(Long id) {
        return Optional.ofNullable(entityManager.find(FriendsList.class, id));
    }

    public List<FriendsList> findAll() {
        return entityManager.createQuery("SELECT fl FROM friends_lists fl", FriendsList.class)
                .getResultList();
    }

    public void save(FriendsList friendsList) {
        entityManager.persist(friendsList);
    }

    public void updateFriendsList(FriendsList friendsList) {
        entityManager.merge(friendsList);
    }

    public void deleteFriendsList(FriendsList friendsList) {
        entityManager.remove(entityManager.contains(friendsList) ? friendsList : entityManager.merge(friendsList));
    }

    public List<FriendsList> findByIsClose(Boolean isClose) {
        return entityManager.createQuery("SELECT fl FROM friends_lists fl WHERE fl.isClose = :isClose", FriendsList.class)
                .setParameter("isClose", isClose)
                .getResultList();
    }

    public List<FriendsList> findByFriendSinceGreaterThanEqual(Date friendSince) {
        return entityManager.createQuery("SELECT fl FROM friends_lists fl WHERE fl.friendSince >= :friendSince", FriendsList.class)
                .setParameter("friendSince", friendSince)
                .getResultList();
    }
}
