package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Comment;
import email.com.gmail.ttsai0509.cruxer.repository.CommentRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.view.CommentViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/comments")
public class CommentRestController {

    @Autowired private AccountService accountService;
    @Autowired private CommentRepository commentRepo;

    @JsonView(CommentViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Comment putComment(
            @PathVariable("id") String id,
            @RequestParam(required = true) String text
    ) {

        Comment comment = commentRepo.getOne(id);

        Account account = accountService.getCurrentAccount();

        if (!account.isOwnerOf(comment))
            throw new AccessDeniedException("You do not have permission to perform this action.");

        return commentRepo.save(comment);

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
