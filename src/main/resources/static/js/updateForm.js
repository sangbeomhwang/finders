document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tabs button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // 탭 버튼 활성화 상태 변경
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            // 탭 콘텐츠 표시 상태 변경
            const target = this.getAttribute('data-tab');
            tabContents.forEach(content => {
                if (content.id === target) {
                    content.classList.add('active');
                } else {
                    content.classList.remove('active');
                }
            });
        });
    });

    // 프로필 이미지 미리보기
    document.querySelectorAll('input[type="file"]').forEach(input => {
        input.addEventListener('change', function(event) {
            const file = event.target.files[0];
            const preview = document.getElementById(`${event.target.id}-preview`);

            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                }
                reader.readAsDataURL(file);
            } else {
                preview.src = 'default-profile.png';
            }
        });
    });

    // 선택한 기술들을 저장할 배열
    let selectedTechs = [];

    // 기술 버튼 클릭 시 호출되는 함수
    window.toggleTech = function(element) {
        const tech = element.getAttribute('data-tech');
        element.classList.toggle('selected');

        const index = selectedTechs.indexOf(tech);
        if (index === -1) {
            selectedTechs.push(tech);
        } else {
            selectedTechs.splice(index, 1);
        }
    }

    // 회원가입 폼 제출 시 유효성 검사 수정
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(event) {
            // 프리랜서 회원가입 폼인지 확인
            const isFreelancerForm = this.id === 'freelancer-update-form';

            // 프리랜서 폼이고 기술이 선택되지 않은 경우에만 경고 메시지 표시
            if (isFreelancerForm && selectedTechs.length === 0) {
                alert('최소 하나 이상의 기술을 선택해야 합니다.');
                event.preventDefault(); // 폼 제출 방지
            } else if (isFreelancerForm) {
                // 프리랜서 폼인 경우에만 선택한 기술들을 hidden input에 저장
                document.getElementById('selectedSkills').value = selectedTechs.join(',');
            }
            // 클라이언트(기업) 폼인 경우 추가적인 검사 없이 제출 허용
        });
    });
});