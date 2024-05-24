package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {
    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    // below methods, both of them are correct , We can use which one we want.

    public <T> T convert(Object objectToBeConverted, T convertedObject){
        return modelMapper.map(objectToBeConverted,(Type) convertedObject.getClass());
    }


    public <T> T convert(Object objectToBeConverted, Class<T> convertedObject )  {

        return  modelMapper.map(objectToBeConverted, convertedObject);
    }




}
