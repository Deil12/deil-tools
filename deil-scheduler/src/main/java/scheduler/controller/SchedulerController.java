package scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import scheduler.login.SaQuickManager;

@Controller
public class SchedulerController {

    @RequestMapping("/")
    public String index(Model model) {
        // model.addAttribute()
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("cfg", SaQuickManager.getConfig());
        return "sa-login";
    }

    @RequestMapping("/track")
    public String track(Model model) {
        model.addAttribute("data", "{\n" +
                "\t\"logistics_tracks\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"logistics_no\":\"784-45865556\",\n" +
                "\t\t\t\"box_num\":10,\n" +
                "\t\t\t\"box_weight\":100000,\n" +
                "\t\t\t\"current_track\":{\n" +
                "\t\t\t\t\"location\":\"CAN\",\n" +
                "\t\t\t\t\"content\":\"DEP\",\n" +
                "\t\t\t\t\"date\":\"2024-02-28T04:43:00+08:00\",\n" +
                "\t\t\t\t\"track_code\":\"ori_flight_departure\",\n" +
                "\t\t\t\t\"box_num\":10,\n" +
                "\t\t\t\t\"box_weight\":100000,\n" +
                "\t\t\t\t\"flight_no\":\"CZ2529\",\n" +
                "\t\t\t\t\"extend\":null\n" +
                "\t\t\t},\n" +
                "\t\t\t\"tracks\":[\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"location\":\"CAN\",\n" +
                "\t\t\t\t\t\"content\":\"DEP\",\n" +
                "\t\t\t\t\t\"date\":\"2024-02-28T04:43:00+08:00\",\n" +
                "\t\t\t\t\t\"track_code\":\"ori_flight_departure\",\n" +
                "\t\t\t\t\t\"box_num\":10,\n" +
                "\t\t\t\t\t\"box_weight\":100000,\n" +
                "\t\t\t\t\t\"flight_no\":\"CZ2529\",\n" +
                "\t\t\t\t\t\"extend\":null\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}");
        return "track";
    }

}
