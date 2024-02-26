
# DevPlace Manager

## 소개

DevPlace Manager는 원격 개발 환경(RDE)을 구성하고 운영하기 위해 설계되었습니다. VSCODE, SSH Server, Jupyter Notebook 환경을 IdeConfig Custom Resource를 기반으로 하는 statefulset의 생성 및 관리를 지원합니다. Frontend에서 요청하는 작업 공간 생성, RDE 컨테이너 생성, 라우터 등록 등의 작업을 Operator와의 통신을 통해 수행합니다.

## 목차

- [소개](#소개)
- [설치](#설치)
- [사용법](#사용법)
- [의존성](#의존성)
- [구성](#구성)
- [기여자](#기여자)
- [라이선스](#라이선스)

## 설치

설치 과정은 `devplace-script` 디렉토리에 위치한 `cicd.sh` 스크립트를 실행하는 것을 포함합니다. `cicd.sh -a` (모두)를 실행하면 Spring 코드 빌드, Docker 빌드 & 푸시, 배포 YAML 생성, 클러스터 배포 등의 일련의 작업이 트리거됩니다:

```bash
./cicd.sh -a
```

## 사용법

DevPlace Manager는 Kubernetes에서 실행하기 위한 것입니다. 배포 및 서비스 구성은 동봉된 `k8s.tar` 아카이브에서 제공됩니다.

배포를 위해, `cicd.sh -a` 또는 `cicd.sh -y`를 실행하여 `env.properties`에 정의된 변수를 사용해 `*.t` 템플릿을 처리합니다. 이는 템플릿의 플레이스홀더를 실제 값으로 대체하여 Kubernetes 배포를 위한 `deploy.yaml`과 같은 파일을 생성합니다:

```bash
./cicd.sh -y  # 또는 전체 과정을 위해 -a
```

필요한 YAML 파일을 생성한 후, Kubernetes 클러스터에 배포합니다:

```bash
kubectl apply -f deploy.yaml
kubectl apply -f service.yaml
```

## 의존성

DevPlace Manager는 `devplace-operator` 및 `devplace-proxy`와 같은 외부 구성 요소와 상호 작용합니다. 이러한 의존성이 귀하의 Kubernetes 클러스터 내에서 적절히 구성되고 액세스 가능한지 확인하세요.

## 구성

구성 매개변수는 `env.properties`에 지정됩니다. 이 파일은 배포 환경의 구체적인 사항에 맞게 조정되어야 합니다.

```properties
JAR_FILE_PATH="./target/idemanager-0.0.1-SNAPSHOT.jar"
DOCKER_REGISTRY="{IMAGE-REGISTRY-URL}/{PROJECT_NAME}"
DOCKER_REGISTRY_USER=""
DOCKER_REGISTRY_PASSWORD=""
DOCKER_CACHE="--no-cache"
IMAGE_NAME="ide-manager"
VERSION="1.0.0"
DEPLOY_PATH="./k8s"
DEPLOY_FILE_NAME=deploy.yaml
LOGGING_LEVEL=DEBUG
```

## Q&A
DevPlace에 대한 사용에 대한 질문이나 Cloud Native Application 과 Microservice 전환에 대한 질문은 다음 메일로 문의 주시기 바랍니다.

yongwoo.yi0@gmail.com

# License
이 프로젝트는 Apache License 2.0에 따라 라이선스가 부여됩니다. 자세한 내용은 [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) 파일을 참고하세요.
