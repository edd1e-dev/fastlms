package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.entity.RegisterCourse;
import com.zerobase.fastlms.course.entity.RegisterCourseCode;
import com.zerobase.fastlms.course.mapper.RegisterCourseMapper;
import com.zerobase.fastlms.course.model.RegisterCourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.repository.RegisterCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterCourseServiceImpl implements RegisterCourseService {

    private final RegisterCourseRepository registerCourseRepository;
    private final RegisterCourseMapper registerCourseMapper;


    @Override
    public List<RegisterCourseDto> getList(RegisterCourseParam parameter) {

        long totalCount = registerCourseMapper.selectListCount(parameter);

        List<RegisterCourseDto> list = registerCourseMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (RegisterCourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public RegisterCourseDto detail(long id) {

        Optional<RegisterCourse> optionalTakeCourse = registerCourseRepository.findById(id);
        if (optionalTakeCourse.isPresent()) {
            return RegisterCourseDto.of(optionalTakeCourse.get());
        }
        return null;
    }

    @Override
    public ServiceResult updateStatus(long id, String status) {

        Optional<RegisterCourse> optionalTakeCourse = registerCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }

        RegisterCourse registerCourse = optionalTakeCourse.get();

        registerCourse.setStatus(status);
        registerCourseRepository.save(registerCourse);

        return new ServiceResult(true);
    }

    @Override
    public List<RegisterCourseDto> getMyCourse(String userId) {

        RegisterCourseParam registerCourseParam = new RegisterCourseParam();
        registerCourseParam.setUserId(userId);
        List<RegisterCourseDto> list = registerCourseMapper.selectListMyCourse(registerCourseParam);

        return list;
    }

    @Override
    public ServiceResult cancel(long id) {

        Optional<RegisterCourse> optionalTakeCourse = registerCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }

        RegisterCourse registerCourse = optionalTakeCourse.get();

        registerCourse.setStatus(RegisterCourseCode.STATUS_CANCEL);
        registerCourseRepository.save(registerCourse);

        return new ServiceResult();
    }
}
