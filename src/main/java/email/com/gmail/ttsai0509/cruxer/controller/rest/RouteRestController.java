package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.exception.CruxerException;
import email.com.gmail.ttsai0509.cruxer.exception.InternalException;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.HoldInstance;
import email.com.gmail.ttsai0509.cruxer.model.Route;
import email.com.gmail.ttsai0509.cruxer.model.WallInstance;
import email.com.gmail.ttsai0509.cruxer.repository.*;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.service.FileService;
import email.com.gmail.ttsai0509.cruxer.service.file.LocalFileService;
import email.com.gmail.ttsai0509.cruxer.view.RouteViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/routes")
public class RouteRestController {

    @Autowired private AccountService accountService;
    @Autowired private FileService fileService;
    @Autowired private RouteRepository routeRepo;
    @Autowired private HoldRepository holdRepo;
    @Autowired private WallRepository wallRepo;
    @Autowired private HoldInstanceRepository holdInstanceRepo;
    @Autowired private WallInstanceRepository wallInstanceRepo;

    @JsonView(RouteViews.Details.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Route> getRoutes(
            Pageable pageable
    ) {

        return routeRepo.findAll(pageable).getContent();

    }

    @JsonView(RouteViews.Complete.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Route getRoute(
            @PathVariable("id") String id
    ) {

        return routeRepo.findOne(id);

    }

    @JsonView(RouteViews.Complete.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Route putRoute(
            @PathVariable String id
    ) throws IOException {

        Route route = routeRepo.findOne(id);
        if (route == null)
            throw new ResourceNotFoundException("");

        Account account = accountService.getCurrentAccount();
        if (account == null || !account.isOwnerOf(route))
            throw new AccessDeniedException("");

        throw new InternalException("Not yet implemented.");

    }

    @Transactional
    @JsonView(RouteViews.Complete.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Route postRoute(
            @RequestBody Route route
    ) throws IOException {

        if(route.getWallInstances() == null || route.getWallInstances().size() < 1) {
            throw new CruxerException("No wall instances.");
        }

        for (WallInstance wallInstance : route.getWallInstances()) {
            String wallId = wallInstance.getWall().getId();
            if (wallRepo.findOne(wallId) == null)
                throw new ResourceNotFoundException("");

            for (HoldInstance holdInstance : wallInstance.getHoldInstances()) {
                String holdId = holdInstance.getHold().getId();
                if (holdRepo.findOne(holdId) == null)
                    throw new ResourceNotFoundException("");
            }
        }

        String thumbnail = fileService.saveBase64(route.getThumbnailRaw(), LocalFileService.Base64DataType.PNG);
        if (thumbnail == null || thumbnail.isEmpty())
            throw new ResourceNotFoundException("");
        route.setThumbnail(thumbnail);

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        Route persistedRoute = Route.createRoute(
                route.getName(),
                thumbnail,
                account,
                route.getWallInstances(),
                route.getGrade()
        );
        routeRepo.save(persistedRoute);

        for (WallInstance wallInstance : route.getWallInstances()) {
            wallInstance.setId(null);
            wallInstance.setRoute(persistedRoute);
            wallInstanceRepo.save(wallInstance);

            for (HoldInstance holdInstance : wallInstance.getHoldInstances()) {
                holdInstance.setId(null);
                holdInstance.setWallInstance(wallInstance);
                holdInstanceRepo.save(holdInstance);
            }
        }

        return persistedRoute;
    }

}
