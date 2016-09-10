package ru.ps.geoloc.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ps.geoloc.web.domain.CreateUpdateCustomerRequest;
import ru.ps.geoloc.web.exceptions.LoadingGeoDataFailedException;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.ps.geoloc.processors.InitProcessor;
import ru.ps.geoloc.processors.CustomerProcessor;


@RestController
public class CustomerService {
    private final static AtomicBoolean isInitialized = new AtomicBoolean(false);

    @Autowired
    private CustomerProcessor customerProcessor;

    @Autowired
    private InitProcessor initProcessor;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String init(@RequestParam("network") String networkFileName, @RequestParam("label") String geoLabelsFileName) throws LoadingGeoDataFailedException, IOException {
        if (!isInitialized.get()) {
            if(networkFileName != null && !networkFileName.isEmpty() && geoLabelsFileName != null && !geoLabelsFileName.isEmpty()) {
                String result = initProcessor.init(networkFileName, geoLabelsFileName);
                isInitialized.set(true);
                return result;
            } else {
                throw new LoadingGeoDataFailedException("One of input data file is empty networkFileName=" + networkFileName + " geoLabelsFileName=" + geoLabelsFileName);
            }
        }
        return null;
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String createNewUser(@RequestBody CreateUpdateCustomerRequest createUpdateCustomerRequest) {
        return customerProcessor.createNewCustomer(createUpdateCustomerRequest);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String updateExistingUser(@RequestBody CreateUpdateCustomerRequest createUpdateCustomerRequest) {
        return customerProcessor.updateCustomer(createUpdateCustomerRequest);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String deleteUser(@PathVariable Optional<String> userId) {
        StringBuilder result = new StringBuilder("");
        userId.ifPresent(value -> result.append(customerProcessor.deleteCustomer(Integer.parseInt(userId.get()))));
        return result.toString();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String findUserLocation(@PathVariable Optional<String> userId) {
        StringBuilder result = new StringBuilder("");
        userId.ifPresent(value -> result.append(customerProcessor.findCustomerLocation(Integer.parseInt(userId.get()))));
        return result.toString();
    }

    @RequestMapping(value = "/cell", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String findNumberOfUsersInCell(@RequestParam("x") Optional<String> titleX, @RequestParam("y") Optional<String> titleY) {
        StringBuilder result = new StringBuilder("");
        if (titleX.isPresent() && titleY.isPresent()) {
            result.append(customerProcessor.findNumberOfUsersInCell(Integer.parseInt(titleX.get()), Integer.parseInt(titleY.get())));
        }
        return result.toString();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public @ResponseBody String findNumberOfUsers() {
        StringBuilder result = new StringBuilder("");
        result.append(customerProcessor.findNumberOfCustomers());
        return result.toString();
    }



    @RequestMapping(value = "echo", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(){
        return "ECHO TEST!";
    }
}
