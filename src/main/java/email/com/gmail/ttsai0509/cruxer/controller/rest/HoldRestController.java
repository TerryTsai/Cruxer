package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Hold;
import email.com.gmail.ttsai0509.cruxer.repository.HoldRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.service.FileService;
import email.com.gmail.ttsai0509.cruxer.view.HoldViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RestController
@Secured({"ROLE_USER"})
@RequestMapping("/holds")
public class HoldRestController {

    @Autowired private FileService fileService;
    @Autowired private AccountService accountService;
    @Autowired private HoldRepository holdRepo;

    @JsonView(HoldViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Hold> getHolds(Pageable pageable) {

        return holdRepo.findAll(pageable).getContent();

    }

    @JsonView(HoldViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Hold getHold(@PathVariable String id) {

        return holdRepo.findOne(id);

    }

    @JsonView(HoldViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Hold postHold(@RequestParam MultipartFile file) {

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        String model = fileService.saveFile(file, ".obj");
        if (model == null || model.isEmpty())
            throw new ResourceNotFoundException("");

        Hold hold = new Hold(account, model);
        return holdRepo.save(hold);

    }

    @JsonView(HoldViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Hold putHold(@PathVariable String id, @RequestParam MultipartFile file) {

        Hold hold = holdRepo.findOne(id);
        if (hold == null)
            throw new ResourceNotFoundException("");

        Account account = accountService.getCurrentAccount();
        if (!account.isOwnerOf(hold))
            throw new AccessDeniedException("");

        String model = fileService.saveFile(file, ".obj");
        if (model == null || model.isEmpty())
            throw new ResourceNotFoundException("");

        hold.setModel(model);
        return holdRepo.save(hold);

    }

}
