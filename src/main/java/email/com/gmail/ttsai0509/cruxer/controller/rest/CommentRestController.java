package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.*;
import email.com.gmail.ttsai0509.cruxer.repository.CommentRepository;
import email.com.gmail.ttsai0509.cruxer.repository.HoldRepository;
import email.com.gmail.ttsai0509.cruxer.repository.RouteRepository;
import email.com.gmail.ttsai0509.cruxer.repository.WallRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.view.CommentViews;
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
@RequestMapping("/comments")
public class CommentRestController {

    @Autowired private AccountService accountService;
    @Autowired private CommentRepository commentRepo;
    @Autowired private RouteRepository routeRepo;
    @Autowired private HoldRepository holdRepo;
    @Autowired private WallRepository wallRepo;

    @JsonView(CommentViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Comment> getComments(
            @RequestParam String type,
            @RequestParam String id,
            Pageable pageable
    ) {

        switch (type) {
            case "route":
                Route route = routeRepo.findOne(id);
                if (route == null)
                    throw new ResourceNotFoundException("");
                else
                    return commentRepo.findByRoute(route, pageable).getContent();

            case "hold":
                Hold hold = holdRepo.findOne(id);
                if (hold == null)
                    throw new ResourceNotFoundException("");
                else
                    return commentRepo.findByHold(hold, pageable).getContent();

            case "wall":
                Wall wall = wallRepo.findOne(id);
                if (wall == null)
                    throw new ResourceNotFoundException("");
                else
                    return commentRepo.findByWall(wall, pageable).getContent();

            default:
                throw new ResourceNotFoundException("");
        }

    }

    @JsonView(CommentViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Comment putComment(
            @PathVariable("id") String id,
            @RequestParam String comment
    ) {

        Comment commentObj = commentRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(commentObj))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        commentObj.setText(comment);

        return commentRepo.save(commentObj);

    }

    @JsonView(CommentViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Comment postComment(
            @RequestParam String type,
            @RequestParam String id,
            @RequestParam String comment
    ) {

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        Comment commentObj = null;
        switch (type) {
            case "route":
                Route route = routeRepo.findOne(id);
                commentObj = Comment.createRouteComment(account, route, comment);
                break;

            case "hold":
                Hold hold = holdRepo.findOne(id);
                commentObj = Comment.createHoldComment(account, hold, comment);
                break;

            case "wall":
                Wall wall = wallRepo.findOne(id);
                commentObj = Comment.createWallComment(account, wall, comment);
                break;
        }

        if (commentObj == null)
            throw new ResourceNotFoundException("");

        return commentRepo.save(commentObj);

    }

    @JsonView(CommentViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean deleteComment(
            @PathVariable("id") String id
    ) {
        Comment comment = commentRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(comment))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        commentRepo.delete(comment);

        return true;

    }
}
