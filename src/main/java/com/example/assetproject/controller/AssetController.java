package com.example.assetproject.controller;

import com.example.assetproject.dto.HardwareAssetDTO;
import com.example.assetproject.dto.SoftwareAssetDTO;
import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.PaginationModel;
import com.example.assetproject.entity.Software;
import com.example.assetproject.form.AssetHardwareAddForm;
import com.example.assetproject.form.AssetHardwareUpdateForm;
import com.example.assetproject.form.AssetSoftwareAddForm;
import com.example.assetproject.form.AssetSoftwareUpdateForm;
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
import org.springframework.web.bind.annotation.*;
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
//    @GetMapping("/list/asset")
//    public String assets(Model model){
//        List<Asset> assets = assetService.findAll();
//        model.addAttribute("assets", assets);
//        return "asset/list/asset";
//    }

    //자산리스트 조회(페이징처리)
    @GetMapping("/list/asset")
    public String assets(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(required = false) String category,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String sort,
                         @RequestParam(required = false, defaultValue = "asc") String order,
                         Model model) {

        // 페이지 번호가 1 미만인 경우 1로 강제 설정
        page = Math.max(page, 1);

        int pageSize = 10; // 페이지당 보여줄 아이템 수
        int totalItems = assetService.countAllFiltered(category, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize); // 전체 페이지 수 계산

        List<Asset> assets = assetService.findAllWithPagingAndFilter(page, pageSize, category, keyword, sort, order);
        PaginationModel pagination = new PaginationModel(page, totalPages, totalItems, pageSize);

        model.addAttribute("assets", assets);
        model.addAttribute("pagination", pagination); // 페이징 정보 모델 추가
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        return "asset/list/asset";
    }


    //소프트웨어 자산리스트 조회
//    @GetMapping("/list/software")
//    public String software(Model model){
//        List<Software> softwares = softwareService.findAll();
//        model.addAttribute("softwares", softwares);
//        return "asset/list/software";
//    }

    //소프트웨어 자산리스트 조회(페이징처리)
    @GetMapping("/list/software")
    public String software(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String sort,
                           @RequestParam(required = false, defaultValue = "asc") String order,
                           Model model) {

        // 페이지 번호가 1 미만인 경우 1로 강제 설정
        page = Math.max(page, 1);

        int pageSize = 10; // 페이지당 보여줄 아이템 수
        int totalItems = softwareService.countAllFiltered(category, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize); // 전체 페이지 수 계산

        List<Software> softwares = softwareService.findAllWithPagingAndFilter(page, pageSize, category, keyword, sort, order);
        PaginationModel pagination = new PaginationModel(page, totalPages, totalItems, pageSize);

        model.addAttribute("softwares", softwares);
        model.addAttribute("pagination", pagination); // 페이징 정보 모델 추가
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        return "asset/list/software";
    }

    //하드웨어 자산리스트 조회
//    @GetMapping("/list/hardware")
//    public String hardware(Model model){
//        List<Hardware> hardwares = hardwareService.findAll();
//        model.addAttribute("hardwares", hardwares);
//        return "asset/list/hardware";
//    }

    // //하드웨어 자산리스트 조회(페이징처리)
    @GetMapping("/list/hardware")
    public String hardware(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String sort,
                           @RequestParam(required = false, defaultValue = "asc") String order,
                           Model model) {

        // 페이지 번호가 1 미만인 경우 1로 강제 설정
        page = Math.max(page, 1);

        int pageSize = 10; // 페이지당 보여줄 아이템 수
        int totalItems = hardwareService.countAllFiltered(category, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        List<Hardware> hardwares = hardwareService.findAllWithPagingAndFilter(page, pageSize, category, keyword, sort, order);
        PaginationModel pagination = new PaginationModel(page, totalPages, totalItems, pageSize);

        model.addAttribute("hardwares", hardwares);
        model.addAttribute("pagination", pagination);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

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

    // 자산 타입 확인 및 수정 폼으로 리다이렉트
    @GetMapping("/{assetIdx}/edit/asset")
    public String editAssetForm(@PathVariable Long assetIdx, Model model) {
        // 자산 코드 확인
        String assetCode = assetService.findAssetCodeById(assetIdx);

        if (assetCode != null) {
            // 자산 코드에서 접두어 추출 (예: "HW", "SW")
            String assetPrefix = assetCode.substring(0, 2);

            if ("HW".equals(assetPrefix)) {

                Long hardwareIdx =hardwareService.findHardwareIdxByAssetIdx(assetIdx);
                HardwareAssetDTO hardware = hardwareService.findHardwareById(hardwareIdx);
                model.addAttribute("hardware", hardware);
                return "asset/edit/editHardwareForm";
            } else if ("SW".equals(assetPrefix)) {

                Long softwareIdx =softwareService.findSoftwareIdxByAssetIdx(assetIdx);
                SoftwareAssetDTO software = softwareService.findSoftwareById(softwareIdx);
                model.addAttribute("software", software);
                return "asset/edit/editSoftwareForm";
            }
        }

        // 자산 코드가 null이거나 기대하는 형식이 아닌 경우 적절한 에러 처리 또는 기본 페이지로 리다이렉트
        return "redirect:/assets/list/asset";
    }


    //하드웨어 자산 수정 폼
    @GetMapping("/{hardwareIdx}/edit/hardware")
    public String editHardwareForm(@PathVariable Long hardwareIdx, Model model){

        HardwareAssetDTO hardware = hardwareService.findHardwareById(hardwareIdx);

        model.addAttribute("hardware", hardware);

        return "asset/edit/editHardwareForm";
    }

    //하드웨어 자산 수정
    @PostMapping("/{hardwareIdx}/edit/hardware")
    @Transactional
    public String edit(@PathVariable Long hardwareIdx, @Validated @ModelAttribute("hardware") AssetHardwareUpdateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            bindingResult.addError(new ObjectError("hardware", new String[]{"hardwareEdit"}, null, null));
            return "asset/edit/editHardwareForm";
        }

        // Perform the update operation
        hardwareService.update(hardwareIdx, form);

        // Add a flash attribute to show a success message on the redirect target
        redirectAttributes.addFlashAttribute("successMessage", "Hardware updated successfully!");

        return "redirect:/assets/list/hardware";
    }


    //소프트웨어 자산 수정 폼
    @GetMapping("/{softwareIdx}/edit/software")
    public String editSoftwareForm(@PathVariable Long softwareIdx, Model model){

        SoftwareAssetDTO software = softwareService.findSoftwareById(softwareIdx);

        model.addAttribute("software", software);
        return "asset/edit/editSoftwareForm";
    }

    //소프트웨어 자산 수정
    @PostMapping("/{softwareIdx}/edit/software")
    public String edit(@PathVariable Long softwareIdx, @Validated @ModelAttribute("software") AssetSoftwareUpdateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            bindingResult.addError(new ObjectError("software", new String[]{"softwareEdit"}, null, null));
            return "asset/edit/editSoftwareForm";
        }

        // Perform the update operation
        softwareService.update(softwareIdx, form);

        // Add a flash attribute to show a success message on the redirect target
        redirectAttributes.addFlashAttribute("successMessage", "Software updated successfully!");
        return "redirect:/assets/list/software";

    }

    //하드웨어 자산 삭제
    @PostMapping("/deleteSelectedHardware")
    @Transactional
    public String deleteSelectedHardware(@RequestParam("hardwareIds") List<Long> hardwareIds, RedirectAttributes redirectAttributes) {

        // Hardware 자산 삭제
        hardwareService.deleteSelectedHardware(hardwareIds);
        redirectAttributes.addFlashAttribute("successMessage", "Selected hardware assets have been successfully deleted.");
        return "redirect:/assets/list/hardware";
    }

    //소프트웨어 자산 삭제
    @PostMapping("/deleteSelectedSoftware")
    @Transactional
    public String deleteSelectedSoftware(@RequestParam("softwareIds") List<Long> softwareIds, RedirectAttributes redirectAttributes) {

        softwareService.deleteSelectedSoftware(softwareIds);
        redirectAttributes.addFlashAttribute("successMessage", "Selected software assets have been successfully deleted.");
        return "redirect:/assets/list/software";
    }

    //자산 삭제
    @PostMapping("/deleteSelectedAsset")
    @Transactional
    public String deleteSelectedAsset(@RequestParam("assetIds") List<Long> assetIds, RedirectAttributes redirectAttributes) {

        assetService.deleteSelectedAsset(assetIds);
        redirectAttributes.addFlashAttribute("successMessage", "Selected software assets have been successfully deleted.");
        return "redirect:/assets/list/asset";
    }

}