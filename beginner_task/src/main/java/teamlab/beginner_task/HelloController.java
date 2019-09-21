package teamlab.beginner_task;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

@RestController
public class HelloController {
    @RequestMapping(value="/screen", method= RequestMethod.GET)
    public ModelAndView screen(ModelAndView mv) {
        mv.setViewName("screen");
        return mv;
    }
    @RequestMapping(value="/index", method= RequestMethod.GET)
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

    @RequestMapping("hello")
    private String hello() {
        return "SpringBoot!";
    }

    @RequestMapping("/test/{param}")
    private String testPathVariable( @PathVariable String param ) {
        return "受け取ったパラメータ：" + param;
    }

    @RequestMapping( value = "hogemoge4",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public Resource file() {
        return new FileSystemResource( new File("C:\\test\\hogemoge.png") );
    }
}
