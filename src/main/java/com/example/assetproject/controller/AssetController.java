package com.example.assetproject.controller;

import com.example.assetproject.dto.Asset;
import com.example.assetproject.dto.Hardware;
import com.example.assetproject.dto.Software;
import com.example.assetproject.form.AssetHardwareAddForm;
import com.example.assetproject.form.AssetSoftwareAddForm;
import com.example.assetproject.service.AssetService;
import com.example.assetproject.service.HardwareService;
import com.example.assetproject.service.SoftwareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;
    private final SoftwareService softwareService;
    private final HardwareService hardwareService;

    //자산리스트 조회
    @GetMapping("/list/asset")
    public String assets(Model model){
        List<Asset> assets = assetService.findAll();
        model.addAttribute("assets", assets);
        return "asset/list/asset";
    }

    //하드웨어 자산리스트 조회
    @GetMapping("/list/software")
    public String software(Model model){
        List<Software> softwares = softwareService.findAll();
        model.addAttribute("softwares", softwares);
        return "asset/list/software";
    }

    //소프트웨어 자산리스트 조회
    @GetMapping("/list/hardware")
    public String hardware(Model model){
        List<Hardware> hardwares = hardwareService.findAll();
        model.addAttribute("hardwares", hardwares);
        return "asset/list/hardware";
    }

    //하드웨어 자산 등록페이지
    @GetMapping("/add/hardware")
    public String addHardwareForm(Model model)
    {
        model.addAttribute("asset", new Hardware());
        return "asset/add/addHardwareForm";
    }

    //소프트웨어 자산 등록페이지
    @GetMapping("/add/software")
    public String addSoftwareForm(Model model){
        model.addAttribute("asset", new Software());
        return "asset/add/addSoftwareForm";
    }

    //하드웨어 자산 등록
    //asset에 바인딩된 결과가 담김 bindingResult
    //순서 중요! @ModelAttribute hardware asset, BindingResult bindingResult 바로 다음에 나오게!
    @PostMapping("/add/hardware")
    @Transactional
    public String addHardware(@Validated @ModelAttribute("asset") AssetHardwareAddForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {} ", bindingResult);
            bindingResult.addError(new ObjectError("asset",new String[]{"hardwareEnroll"}, null,null));
            return "asset/add/addHardwareForm";
        }

        //성공로직
        Long assetIdx = hardwareService.save(form);
        redirectAttributes.addAttribute("assetIdx", assetIdx);

        return "redirect:/assets/list/hardware";
    }

    //소프트웨어 자산 등록
    @PostMapping("/add/software")
    public String addSoftware(@Validated @ModelAttribute("asset") AssetSoftwareAddForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {} ", bindingResult);
            bindingResult.addError(new ObjectError("asset",new String[]{"softwareEnroll"}, null,null));
            return "asset/add/addSoftwareForm";
        }

        //성공로직
        Long assetIdx = softwareService.save(form);
        redirectAttributes.addAttribute("assetIdx", assetIdx);

        return "redirect:/assets/list/software";
    }

}