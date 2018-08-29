package com.karyasarma.spring_boot_example.controller;

import com.karyasarma.spring_boot_example.dto.Person;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Daniel Joi Partogi Hutapea
 */
@Controller
@RequestMapping("/person")
public class PersonController
{
    private static final Map<Integer, Person> MAP_OF_PERSON = new HashMap<>();

    static
    {
        MAP_OF_PERSON.put(1000, new Person(1000, "Daniel", 10));
        MAP_OF_PERSON.put(1001, new Person(1001, "Joi", 11));
        MAP_OF_PERSON.put(1002, new Person(1002, "Partogi", 12));
        MAP_OF_PERSON.put(1003, new Person(1003, "Hutapea", 13));
    }

    private static int COUNTER_ID;

    public PersonController()
    {
    }

    public synchronized static int generateNewId()
    {
        return ++COUNTER_ID;
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {"application/xml", "application/json"}, produces = {"application/xml", "application/json"})
    @ResponseBody
    public Person insert(@RequestBody Person person)
    {
        int newId = generateNewId();
        person.setId(newId);
        MAP_OF_PERSON.put(newId, person);
        return person;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Person update(@PathVariable Integer id, @RequestBody Person person)
    {
        Person updatedPerson = MAP_OF_PERSON.get(id);

        if(updatedPerson==null)
        {
            throw new RuntimeException(String.format("Person with ID = %d not found.", id));
        }

        updatedPerson.setName(person.getName());
        updatedPerson.setAge(person.getAge());
        return person;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Integer id, HttpServletResponse response)
    {
        Person deletedPerson = MAP_OF_PERSON.get(id);

        if(deletedPerson==null)
        {
            throw new RuntimeException(String.format("Person with ID = %d not found.", id));
        }

        MAP_OF_PERSON.remove(id);

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Person findById(@PathVariable Integer id)
    {
        return MAP_OF_PERSON.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Person> findAll()
    {
        return new ArrayList<>(MAP_OF_PERSON.values());
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Person> search(HttpServletRequest request)
    {
        String name = request.getParameter("name");
        List<Person> listOfFoundPerson = new ArrayList<>();

        for(Map.Entry<Integer, Person> entry : MAP_OF_PERSON.entrySet())
        {
            Person person = entry.getValue();

            if(person.getName().contains(name))
            {
                listOfFoundPerson.add(person);
            }
        }

        return listOfFoundPerson;
    }
}
