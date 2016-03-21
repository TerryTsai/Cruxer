package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.*;
import email.com.gmail.ttsai0509.cruxer.repository.RatingRepository;
import email.com.gmail.ttsai0509.cruxer.repository.RouteRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.view.RatingViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/ratings")
public class RatingRestController {

    @Autowired private AccountService accountService;
    @Autowired private RatingRepository ratingRepo;
    @Autowired private RouteRepository routeRepo;

    @JsonView(RatingViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Rating> getRatings(@RequestParam String routeId, Pageable pageable) {

        Route route = routeRepo.findOne(routeId);
        if (route == null)
            throw new ResourceNotFoundException("");

        return ratingRepo.findByRoute(route, pageable).getContent();

    }

    @JsonView(RatingViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Rating putRating(
            @PathVariable("id") String id,
            @RequestParam(required = false) Scale scale,
            @RequestParam(required = false) Integer grade
    ) {

        Rating ratingObj = ratingRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(ratingObj))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        if (scale != null) {
            ratingObj.setScale(scale);
        }

        if (grade != null) {
            ratingObj.setGrade(grade);
        }

        return ratingRepo.save(ratingObj);

    }

    @JsonView(RatingViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Rating postRating(
            @RequestParam String routeId,
            @RequestParam Scale scale,
            @RequestParam Integer grade
    ) {

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        Route route = routeRepo.findOne(routeId);
        if (route == null)
            throw new ResourceNotFoundException("");

        Rating ratingObj = Rating.createRating(account, route, scale, grade);

        return ratingRepo.save(ratingObj);

    }

    @JsonView(RatingViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean deleteRating(@PathVariable("id") String id) {
        Rating rating = ratingRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(rating))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        ratingRepo.delete(rating);

        return true;

    }
}
