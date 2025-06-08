// sample.cafekiosk.unit.beverage 패키지로 묶어서 관리
// 디렉토리 경로와 같음 : 이 그룹(폴더)에 속한다고 알려주는 선언문
// 다른 패키지에 있는 클래스와 구분
// package 단위로 코드 관리, 이름 충돌 방지, 접근 제한
package sample.cafekiosk.unit.beverage;

// interface는 메서드의 선언부(시그니처)만 있고 실제 구현은 없음
// 클래스는 단일 상속만 가능하지만 인터페이스는 여러개 구현 가능
// 인터페이스의 모든 메서드는 자동으로 public -> getName(), getPrice() 도 public
public interface Beverage {

    String getName();

    int getPrice();

}