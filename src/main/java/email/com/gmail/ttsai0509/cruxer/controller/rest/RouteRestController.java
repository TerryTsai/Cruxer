package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import email.com.gmail.ttsai0509.cruxer.exception.InternalException;
import email.com.gmail.ttsai0509.cruxer.model.*;
import email.com.gmail.ttsai0509.cruxer.repository.*;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.service.FileService;
import email.com.gmail.ttsai0509.cruxer.view.RouteViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/routes")
public class RouteRestController {

    @Autowired private ObjectMapper mapper;
    @Autowired private AccountService accountService;
    @Autowired private FileService fileService;
    @Autowired private RouteRepository routeRepo;
    @Autowired private HoldRepository holdRepo;
    @Autowired private WallRepository wallRepo;
    @Autowired private HoldInstanceRepository holdInstanceRepo;
    @Autowired private WallInstanceRepository wallInstanceRepo;

    @JsonView(RouteViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Route> getRoutes(Pageable pageable) {

        return routeRepo.findAll(pageable).getContent();

    }

    @JsonView(RouteViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Route getRoute(@PathVariable("id") String id) {

        return routeRepo.findOne(id);

    }

    @JsonView(RouteViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Route postRoute(
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String thumbnail,
            @RequestParam(required = true) String walls,
            @RequestParam(required = true) String holds
    ) throws IOException {

        List<WallInstance> wallInstances = Arrays.asList(mapper.readValue(walls, WallInstance[].class));
        List<HoldInstance> holdInstances = Arrays.asList(mapper.readValue(holds, HoldInstance[].class));

        for (WallInstance wallInstance : wallInstances) {
            String wallId = wallInstance.getWall().getId();
            if (wallRepo.findOne(wallId) == null)
                throw new ResourceNotFoundException("");
        }

        for (HoldInstance holdInstance : holdInstances) {
            String holdId = holdInstance.getHold().getId();
            if (holdRepo.findOne(holdId) == null)
                throw new ResourceNotFoundException("");
        }

        String thumbnailUrl = fileService.saveBase64Png(thumbnail);
        if (thumbnailUrl == null || thumbnailUrl.isEmpty())
            throw new ResourceNotFoundException("");

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        Route route = Route.createRoute(name, thumbnailUrl, account, holdInstances, wallInstances);
        routeRepo.save(route);

        route.getHoldInstances().forEach(hi -> {
            hi.setId(null);
            hi.setRoute(route);
            holdInstanceRepo.save(hi);
        });

        route.getWallInstances().forEach(wi -> {
            wi.setId(null);
            wi.setRoute(route);
            wallInstanceRepo.save(wi);
        });

        return route;
    }

    @JsonView(RouteViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Route putRoute(
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String thumbnail,
            @RequestParam(required = false) String walls,
            @RequestParam(required = false) String holds
    ) throws IOException {

        Route route = routeRepo.findOne(id);
        if (route == null)
            throw new ResourceNotFoundException("");

        Account account = accountService.getCurrentAccount();
        if (account == null || !account.isOwnerOf(route))
            throw new AccessDeniedException("");

        throw new InternalException("Not yet implemented.");

    }

}
