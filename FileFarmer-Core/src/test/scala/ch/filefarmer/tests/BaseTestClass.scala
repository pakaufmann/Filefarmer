package ch.filefarmer.tests
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.scalamock.scalatest.MockFactory
import scala.tools.reflect.Mock
import org.scalamock.ProxyMockFactory

abstract class BaseTestClass extends Spec with ShouldMatchers with MockFactory with ProxyMockFactory {
	val currentPath = new java.io.File(".").getCanonicalPath()
}