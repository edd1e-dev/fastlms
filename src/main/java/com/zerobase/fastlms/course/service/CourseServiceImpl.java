package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.entity.RegisterCourse;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.RegisterCourseInput;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.repository.RegisterCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final RegisterCourseRepository registerCourseRepository;
    private final CourseMapper courseMapper;

    private LocalDate getLocalDate(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    public boolean add(CourseInput courseInput) {
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Course course = Course.builder()
                .categoryId(courseInput.getCategoryId())
                .subject(courseInput.getSubject())
                .keyword(courseInput.getKeyword())
                .summary(courseInput.getSummary())
                .contents(courseInput.getContents())
                .price(courseInput.getPrice())
                .salePrice(courseInput.getSalePrice())
                .saleEndDt(saleEndDt)
                .regDt(LocalDateTime.now())
                .filename(courseInput.getFilename())
                .urlFilename(courseInput.getUrlFilename())
                .build();
        courseRepository.save(course);

        return true;
    }

    @Override
    public boolean set(CourseInput courseInput) {
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Optional<Course> optionalCourse = courseRepository.findById(courseInput.getId());
        if (!optionalCourse.isPresent()) {
            return false;
        }

        Course course = optionalCourse.get();
        course.setCategoryId(courseInput.getCategoryId());
        course.setSubject(courseInput.getSubject());
        course.setKeyword(courseInput.getKeyword());
        course.setSummary(courseInput.getSummary());
        course.setContents(courseInput.getContents());
        course.setPrice(courseInput.getPrice());
        course.setSalePrice(courseInput.getSalePrice());
        course.setSaleEndDt(saleEndDt);
        course.setUdtDt(LocalDateTime.now());
        course.setFilename(courseInput.getFilename());
        course.setUrlFilename(courseInput.getUrlFilename());
        courseRepository.save(course);

        return true;
    }

    @Override
    public boolean delete(String idList) {
        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {
                }

                if (id > 0) {
                    courseRepository.deleteById(id);
                }
            }
        }

        return true;
    }

    @Override
    public List<CourseDto> getFrontList(CourseParam courseParam) {

        if (courseParam.getCategoryId() < 1) {
            List<Course> courseList = courseRepository.findAll();
            return CourseDto.of(courseList);
        }

        Optional<List<Course>> optionalCourses =
                courseRepository.findByCategoryId(courseParam.getCategoryId());
        if (optionalCourses.isPresent()) {
            return CourseDto.of(optionalCourses.get());
        }
        return null;
    }

    @Override
    public CourseDto getFrontDetail(long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            return CourseDto.of(optionalCourse.get());
        }
        return null;
    }

    @Override
    public ServiceResult request(RegisterCourseInput registerCourseInput) {
        ServiceResult result = new ServiceResult();

        Optional<Course> optionalCourse =
                courseRepository.findById(registerCourseInput.getCourseId());

        if (!optionalCourse.isPresent()) {
            result.setResult(false);
            result.setMessage("강좌 정보가 존재하지 않습니다.");
            return result;
        }

        Course course = optionalCourse.get();

        //이미 신청정보가 있는지 확인
        String[] statusList = {RegisterCourse.STATUS_REQ, RegisterCourse.STATUS_COMPLETE};
        long count = registerCourseRepository.countByCourseIdAndUserIdAndStatusIn(
                        course.getId(),
                        registerCourseInput.getUserId(),
                        Arrays.asList(statusList));

        if (count > 0) {
            result.setResult(false);
            result.setMessage("이미 신청한 강좌 정보가 존재합니다.");
            return result;
        }

        RegisterCourse takeCourse = RegisterCourse.builder()
                .courseId(course.getId())
                .userId(registerCourseInput.getUserId())
                .payPrice(course.getSalePrice())
                .regDt(LocalDateTime.now())
                .status(RegisterCourse.STATUS_REQ)
                .build();
        registerCourseRepository.save(takeCourse);

        result.setResult(true);
        result.setMessage("");
        return result;
    }

    @Override
    public List<CourseDto> getList(CourseParam courseParam) {
        long totalCount = courseMapper.selectListCount(courseParam);
        List<CourseDto> list = courseMapper.selectList(courseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (CourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - courseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);
    }

    @Override
    public List<CourseDto> getAllList() {
        List<Course> courseList = courseRepository.findAll();
        return CourseDto.of(courseList);
    }
}
