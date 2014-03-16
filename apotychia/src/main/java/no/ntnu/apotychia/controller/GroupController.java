package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<List<Group>>(groupService.getAllGroups(), HttpStatus.OK);
    }

}