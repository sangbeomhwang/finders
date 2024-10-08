let selectedScopes = [];
// 업무 범위 선택 기능 (다중 선택 가능)
function toggleSelect(element) {
    element.classList.toggle('selected');
    const scope = element.querySelector('input[type="checkbox"]').value;

    // 선택된 상태라면 배열에 추가
    if (element.classList.contains('selected')) {
        selectedScopes.push(scope);
    } else {
        // 선택 해제되면 배열에서 제거
        selectedScopes = selectedScopes.filter(s => s !== scope);
    }

    // 숨겨진 input 필드에 선택된 값을 저장 (폼 제출용)
    document.getElementById('selectedWorkScopes').value = selectedScopes.join(',');

    // 모든 recruit select 필드 즉시 업데이트
    updateRecruitSelectOptions();
}

// 모집 인원 select 필드 업데이트 함수
function updateRecruitSelectOptions() {
    const recruitSelects = document.querySelectorAll('select[name="role"]');  // 모든 role select 필드 찾기

    recruitSelects.forEach(select => {
        const selectedValue = select.value; // 현재 선택된 값 저장

        // 기존 옵션 모두 제거
        select.innerHTML = '';  // 옵션 비우기

        // 선택된 업무 범위가 없는 경우 기본 옵션 추가
        if (selectedScopes.length === 0) {
            const option = document.createElement('option');
            option.value = '';
            option.textContent = '업무 범위를 선택해주세요';
            select.appendChild(option);
        } else {
            // 선택된 업무 범위를 기준으로 새로운 옵션 추가
            selectedScopes.forEach(scope => {
                const option = document.createElement('option');
                option.value = scope;
                option.textContent = scope;
                select.appendChild(option);
            });

            // 이전에 선택된 값이 유효하면 복원
            if (selectedValue && selectedScopes.includes(selectedValue)) {
                select.value = selectedValue;
            } else {
                // 유효하지 않으면 기본값 선택
                select.value = selectedScopes[0] || '';
            }
        }
    });
}

let selectedCategories = [];
// 카테고리 선택 기능 (다중 선택 가능)
function toggleCategory(element) {
    element.classList.toggle('selected');
    const category = element.dataset.category;

    // 선택된 상태라면 배열에 추가
    if (element.classList.contains('selected')) {
        selectedCategories.push(category);
    } else {
        // 선택 해제되면 배열에서 제거
        selectedCategories = selectedCategories.filter(c => c !== category);
    }

    // 숨겨진 input 필드에 선택된 카테고리 저장
    document.getElementById('selectedCategories').value = selectedCategories.join(',');

    // 모든 recruit select 필드 즉시 업데이트
    updateRecruitCategoryOptions();
}

// 모집 인원 category select 필드 업데이트 함수
function updateRecruitCategoryOptions() {
    const recruitCategorySelects = document.querySelectorAll('.recruit-team-section select[name="category"], .recruit-added-item select[name="category"]');

    recruitCategorySelects.forEach(select => {
        const selectedValue = select.value; // 현재 선택된 값 저장

        // 기존 옵션 모두 제거
        select.innerHTML = '';

        if (selectedCategories.length === 0) {
            // 선택된 카테고리가 없는 경우 기본 옵션 추가
            const option = document.createElement('option');
            option.value = '';
            option.textContent = '카테고리를 선택해주세요';
            select.appendChild(option);
        } else {
            // 선택된 카테고리를 기준으로 새로운 옵션 추가
            selectedCategories.forEach(category => {
                const option = document.createElement('option');
                option.value = category;
                option.textContent = category;
                select.appendChild(option);
            });

            // 이전에 선택된 값이 새로운 옵션에 존재하면 복원
            if (selectedCategories.includes(selectedValue)) {
                select.value = selectedValue;
            } else {
                select.value = ''; // 존재하지 않으면 선택 초기화
            }
        }
    });
}

// 선택한 기술들을 저장할 배열
let selectedTechs = [];

// 기술 버튼 클릭 시 호출되는 함수
function toggleTech(element) {
    const tech = element.getAttribute('data-tech');  // data-tech 속성에서 기술명 가져오기
    const selectedTechsContainer = document.getElementById('selected-techs');

    // 선택된 상태라면 선택 해제
    if (element.classList.contains('selected')) {
        element.classList.remove('selected');
        selectedTechs = selectedTechs.filter(t => t !== tech);  // 배열에서 제거
    } else {
        // 선택되지 않은 상태라면 선택 추가
        element.classList.add('selected');
        selectedTechs.push(tech);  // 배열에 추가
    }
    // 선택된 기술들을 상단에 표시
    selectedTechsContainer.innerHTML = selectedTechs
        .map(item => `<span class="selected-tech">${item}</span>`)
        .join('');
}

const checkFormInterval = setInterval(function() {
    const form = document.querySelector('form');
    if (form) {
        // 폼에 이벤트 리스너 추가
        form.addEventListener('submit', function(event) {
            const selectedSkillsInput = document.getElementById('selectedSkills');
            selectedSkillsInput.value = selectedTechs.join(',');
        });
        // 폼이 발견되면 더 이상 확인하지 않음
        clearInterval(checkFormInterval);
    }
}, 500);  // 0.5초마다 확인

// 상세 업무 내용 글자 수 카운팅
function countCharacters() {
    const detailTextarea = document.getElementById('detail-text');
    const charCount = document.getElementById('char-count');

    console.log(detailTextarea, charCount);

    detailTextarea.addEventListener('input', function () {
        const length = detailTextarea.value.length;
        charCount.textContent = `${length} / 5000`;
    });
}

// 모집 인원 추가 기능
function addTeamMember() {
    const teamMembersContainer = document.getElementById('recruit-team-members');
    const recruitDeadline = document.getElementById('recruit_deadline').value;
    // 새로운 팀원 입력 필드 생성
    const newTeamMember = document.createElement('div');
    newTeamMember.classList.add('recruit-added-item');
    newTeamMember.innerHTML = `
                <select name="role"></select>
                <select name="category"></select>
                <input type="number" min="1" placeholder="명" name="teamSize[]">
                <button type="button" class="delete-button" onclick="remove(this)">삭제</button>
            `;
    teamMembersContainer.appendChild(newTeamMember);

    // 새로 추가된 팀원의 role select 필드 업데이트
    updateRecruitSelectOptions();
    updateRecruitCategoryOptions();

    document.getElementById('recruit_deadline').value = recruitDeadline;
}

// 사전 검증 질문 추가 기능
function addQuestion() {
    const questionsContainer = document.getElementById('recruit-questions');

    // 새로운 질문 입력 필드 생성
    const newQuestion = document.createElement('div');
    newQuestion.classList.add('recruit-added-question');
    newQuestion.innerHTML = `
                <input type="text" class="recruit-question-input" placeholder="사전 질문을 입력해주세요." name="question[]">
                <button type="button" class = "delete-button" onclick="remove(this)">삭제</button>
            `;
    questionsContainer.appendChild(newQuestion);
}

// Call the character counting function on page load
window.onload = function () {
    countCharacters();
};

function remove(button) {
    const teamMember = button.parentNode;
    teamMember.remove();
}

// DOM이 완전히 로드된 후 실행
document.addEventListener('DOMContentLoaded', function() {
    const sections = document.querySelectorAll(".section");
    const menuItems = document.querySelectorAll("#step-list li");

    // 스크롤 이벤트 리스너
    window.addEventListener("scroll", function() {
        let scrollPosition = window.pageYOffset || document.documentElement.scrollTop;

        sections.forEach((section, index) => {
            const sectionTop = section.offsetTop - 100; // 오프셋 조정
            const sectionHeight = section.offsetHeight;

            if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
                menuItems.forEach(item => item.classList.remove("active"));
                menuItems[index].classList.add("active");
            }
        });
    });

    // 사이드바 항목 클릭 이벤트
    menuItems.forEach(item => {
        item.addEventListener("click", function(e) {
            e.preventDefault();
            const targetId = this.getAttribute("data-target");
            const targetSection = document.getElementById(targetId);
            
            if (targetSection) {
                // 부드러운 스크롤 구현
                window.scrollTo({
                    top: targetSection.offsetTop - 100, // 헤더 높이 등을 고려한 오프셋
                    behavior: 'smooth'
                });
            }
        });
    });
});