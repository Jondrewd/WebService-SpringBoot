package com.jondrewd.course.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.jondrewd.course.entities.User;
import com.jondrewd.course.repositories.UserRepository;
import com.jondrewd.course.services.exceptions.DatabaseException;
import com.jondrewd.course.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityExistsException;

@Component
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public List<User> findAll(){
        return repository.findAll();
    }

    public User findById(long id){
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(obj));
    }
    public User insertUser(User obj){
        return repository.save(obj);
    }
    public void delete(Long id){
        try{
            repository.deleteById(id);
        } 
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(id);
        }
        catch(DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
    public User update(Long id, User obj){
        try{
            User entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityExistsException e) {
            throw new ResourceNotFoundException(id);
         
        }
    }
    private void updateData(User entity, User obj){
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }
}
