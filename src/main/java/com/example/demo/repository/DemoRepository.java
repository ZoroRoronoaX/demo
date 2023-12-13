package com.example.demo.repository;

import com.example.demo.model.Demo;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DemoRepository extends JpaRepository<Demo, Integer>{
   Demo findByPostcode(String postcode);
}