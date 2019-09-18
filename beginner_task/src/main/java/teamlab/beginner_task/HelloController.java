package teamlab.beginner_task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelloController {
    @RequestMapping(value="/", method= RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping(value="/result", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("inputvalue")String inputvalue, ModelAndView mv) {
        mv.setViewName("result");
        mv.addObject("message", inputvalue);
        return mv;
    }
}
