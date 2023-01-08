package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminCourseController extends BaseController {
    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ServletContext servletContext;

    @GetMapping("/admin/course/list")
    public String list(Model model, CourseParam courseParam) {
        courseParam.init();

        List<CourseDto> courseList = courseService.getList(courseParam);
        model.addAttribute("list", courseList);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(courseList)) {
            totalCount = courseList.get(0).getTotalCount();
        }

        String queryString = courseParam.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, courseParam.getPageSize(), courseParam.getPageIndex(), queryString, "");

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }

    @GetMapping(value = {"/admin/course/add", "/admin/course/edit"})
    public String add(Model model,
                      HttpServletRequest request,
                      CourseInput courseInput
    ) {

        model.addAttribute("category", categoryService.getList());

        boolean editMode = request.getRequestURI().contains("/edit");
        CourseDto detail = new CourseDto();

        if (editMode) {
            long id = courseInput.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "admin/course/add";
    }

    String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename) {
        LocalDate now = LocalDate.now();

        String[] dirs = {
                String.format("%s/%d/", baseLocalPath, now.getYear()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue()),
                String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())};

        String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }

        String fileExtension = "";
        if (originalFilename != null) {
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1) {
                fileExtension = originalFilename.substring(dotPos + 1);
            }
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newFilename = String.format("%s%s", dirs[2], uuid);
        String newUrlFilename = String.format("%s%s", urlDir, uuid);
        if (fileExtension.length() > 0) {
            newFilename += "." + fileExtension;
            newUrlFilename += "." + fileExtension;
        }

        return new String[]{newFilename, newUrlFilename};
    }

    @PostMapping(value = {"/admin/course/add", "/admin/course/edit"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            @RequestParam("file") MultipartFile multipartFile,
                            CourseInput courseInput
    ) {
        String saveFilename = "";
        String urlFilename = "";

        if (multipartFile != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String baseLocalPath = "C:/Users/cudy_/Desktop/project1/fastlms/files";
            String baseUrlPath = "/files";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        courseInput.setFilename(saveFilename);
        courseInput.setUrlFilename(urlFilename);

        boolean editMode = request.getRequestURI().contains("/edit");

        if (editMode) {
            long id = courseInput.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = courseService.set(courseInput);
        } else {
            boolean result = courseService.add(courseInput);
        }

        return "redirect:/admin/course/list";
    }

    @PostMapping("/admin/course/delete")
    public String del(Model model,
                      HttpServletRequest request,
                      CourseInput courseInput
    ) {

        boolean result = courseService.delete(courseInput.getIdList());

        return "redirect:/admin/course/list";
    }
}
