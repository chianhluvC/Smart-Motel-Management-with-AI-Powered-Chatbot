package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.Category;
import com.dacn.Nhom8QLPhongTro.repository.ICategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final ICategoryRepository categoryRepository;


    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            Category category = new Category();
            category.setId(1L);
            category.setName("Bình dân");
            category.setDescription("Các phòng đều có bancol cửa sổ thoáng mát." + "Tiện ích, nội thất:" +
                    " Máy lạnh, tủ bếp, gác, giường nệm, tủ lạnh, tủ quần áo, máy giặt sân phơi riêng." +
                    " Đặc biệt: nhà mới xây 100% - Ở được 2 - 3ng thoải mái, phòng rộng đầy đủ tiện ích." +
                    " Rước ngay cho mình một căn phòng trong tháng này để được nhiều ưu đãi, hỗ trợ giữ phòng đến cuối tháng." +
                    " LH xem phòng qua Văn Tài.");
            category.setPrice(3000000.0);
            category.setSize("25");
            category.setQuantityBathRoom(1);
            category.setQuantityBedRoom(2);

            Category category2 = new Category();
            category2.setId(2L);
            category2.setName("Dịch vụ");
            category2.setDescription("Vị trí đẹp, đường Nguyễn Hữu Cảnh, gần Landmark 81, tiện đi Q1, Q2. "+
                    "Phòng tách bếp riêng biệt, rộng 28m2, có cửa sổ trời đón ánh sáng, gió thoáng. "+
                    "Nội thất trang bị sẵn trong phòng đầy đủ, tủ lạnh, máy lạnh, giường, nệm,.."+
                    "Hẻm ô tô, khu dân trí cao, an ninh, yên tĩnh, gần các quán ăn, cafe, trung tâm thương mai, siêu thị,.. "+
                    "Ra vào vân tay, giờ giấc tự do, không chung chủ");
            category2.setPrice(5000000.0);
            category2.setSize("35");
            category2.setQuantityBathRoom(2);
            category2.setQuantityBedRoom(3);


            Category category3 = new Category();
            category3.setId(3L);
            category3.setName("Thương Gia");
            category3.setDescription("Có sân để xe, cổng chính dùng khóa điện tử , giờ giấc tự do. "+
                    "Nội thất như hình. "+ "Máy giặt, máy sấy dùng chung. "+"Cho ở đông người, cho nuôi pet.");
            category3.setPrice(10000000.0);
            category3.setSize("50");
            category3.setQuantityBathRoom(3);
            category3.setQuantityBedRoom(4);

            List<Category> categories = new ArrayList<>();
            categories.add(category);
            categories.add(category2);
            categories.add(category3);

            categoryRepository.saveAll(categories);
        }

    }


    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Category not found on ::"+id));
    }

    public Category getCategoryByName(String name){
        return categoryRepository.findByName(name).orElseThrow(()->
                new RuntimeException("Category not found on ::"+name));
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void updateCategory(@NotNull Category category){
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(()->new IllegalStateException("Category with ID "
                        + category.getId() +" does not exist."));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
    }

    public void deleteCategoryById(Long id){
        if(!categoryRepository.existsById(id)){
            throw new IllegalStateException("Category with ID "+id+" does not exist.");
        }
        categoryRepository.deleteById(id);
    }


}
