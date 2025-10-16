## 멀티모달
- 사용자 메시지 생성시 텍스트와 이미지 또는 오디오와 같은 입력을 함께 구성
- GPT-4o, GPT-5는 이미지 입력은 가능하나 오디오 처리는 불가
- 오디오 입출력이 가능한 모델 선택 gpt-4o-audio-preview, gpt-audio 등...

## 예제
- ImageMultiModalTests - 텍스트와 이미지를 사용하여 사용자 메시지 작성
- AudioMultiModalTests - gpt-audio를 사용한 오디오 입력, 오디오 출력
- AudioController - gpt-audio를 사용한 오디오 출력