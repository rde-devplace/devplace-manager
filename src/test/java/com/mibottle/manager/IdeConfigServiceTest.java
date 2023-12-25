package com.mibottle.manager;

import com.mibottle.manager.model.IdeConfigInfo;
import com.mibottle.manager.repository.IdeConfigInfoRepository;
import com.mibottle.manager.service.IdeConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IdeConfigServiceTest {

    @Mock
    private IdeConfigInfoRepository ideConfigInfoRepository;

    private IdeConfigService ideConfigService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ideConfigService = new IdeConfigService(ideConfigInfoRepository);
    }

    @Test
    void createConfigInfo() {
        IdeConfigInfo ideConfigInfo = new IdeConfigInfo(); // 테스트에 필요한 파라미터 설정
        when(ideConfigInfoRepository.save(any(IdeConfigInfo.class))).thenReturn(ideConfigInfo);

        IdeConfigInfo created = ideConfigService.createConfigInfo(ideConfigInfo);

        assertNotNull(created);
        verify(ideConfigInfoRepository).save(any(IdeConfigInfo.class));
    }

    @Test
    void deleteConfigInfo() {
        String name = "testName";

        ideConfigService.deleteConfigInfo(name);

        verify(ideConfigInfoRepository).deleteById(name);
    }

    // 추가적인 테스트 케이스를 여기에 구현할 수 있습니다.

}

