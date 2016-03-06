package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Rating;
import email.com.gmail.ttsai0509.cruxer.repository.RatingRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.view.RatingViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/ratings")
public class RatingRestController {

    private static interface RatingView extends RatingViews.Id, RatingViews.Scale, RatingViews.Grade {}

    @Autowired private AccountService accountService;
    @Autowired private RatingRepository ratingRepo;

    @JsonView(RatingView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Rating putRating(
            @PathVariable("id") String id,
            @RequestParam(required = true) String text
    ) {

        Rating rating = ratingRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(rating))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        return ratingRepo.save(rating);

    }

    @JsonView(RatingView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean deleteRating(
            @PathVariable("id") String id
    ) {

        Rating rating = ratingRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(rating))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        ratingRepo.delete(rating);

        return true;

    }
}
