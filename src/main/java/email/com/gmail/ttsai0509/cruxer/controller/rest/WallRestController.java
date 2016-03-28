package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Wall;
import email.com.gmail.ttsai0509.cruxer.repository.WallRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.service.FileService;
import email.com.gmail.ttsai0509.cruxer.service.LocalFileService;
import email.com.gmail.ttsai0509.cruxer.view.WallViews;
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
@RequestMapping("/walls")
public class WallRestController {

    @Autowired private FileService fileService;
    @Autowired private AccountService accountService;
    @Autowired private WallRepository wallRepo;

    @JsonView(WallViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Wall> getWalls(
            Pageable pageable
    ) {

        return wallRepo.findAll(pageable).getContent();

    }

    @JsonView(WallViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Wall getWall(
            @PathVariable String id
    ) {

        return wallRepo.findOne(id);

    }

    @JsonView(WallViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Wall putWall(
            @PathVariable String id,
            @RequestParam MultipartFile file
    ) {

        Wall wall = wallRepo.findOne(id);
        if (wall == null)
            throw new ResourceNotFoundException("");

        Account account = accountService.getCurrentAccount();
        if (!account.isOwnerOf(wall))
            throw new AccessDeniedException("");

        String model = fileService.saveFile(file, ".obj");
        if (model == null || model.isEmpty())
            throw new ResourceNotFoundException("");

        wall.setModel(model);
        return wallRepo.save(wall);

    }

    @JsonView(WallViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Wall postWall(
            @RequestParam MultipartFile file,
            @RequestParam String thumbnail
    ) {

        Account account = accountService.getCurrentAccount();
        if (account == null)
            throw new AccessDeniedException("");

        String model = fileService.saveFile(file, ".obj");
        if (model == null || model.isEmpty())
            throw new ResourceNotFoundException("");

        String thumb = fileService.saveBase64(thumbnail, LocalFileService.Base64DataType.JPEG);
        if (thumb == null || thumb.isEmpty())
            throw new ResourceNotFoundException("");

        Wall wall = new Wall(account, model, thumb);
        return wallRepo.save(wall);

    }

}
