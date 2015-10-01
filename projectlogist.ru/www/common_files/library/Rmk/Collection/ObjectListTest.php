<?php
/**
 * Автотест списка объектов общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\ObjectList,
    stdClass,
    Exception,
    PHPUnit_Framework_TestCase;

class ObjectListTest extends PHPUnit_Framework_TestCase
{

    /**
     * Тестовый объект.
     *
     * @var stdClass
     */
    protected $object1;

    /**
     * Тестовый объект.
     *
     * @var stdClass
     */
    protected $object2;

    /**
     * Тестовый объект.
     *
     * @var stdClass
     */
    protected $object3;

    /**
     * Тестовый массив.
     *
     * @var array
     */
    protected $array;

    /**
     * Карта общего назначения.
     *
     * @var ObjectList
     */
    protected $list;

    public function setUp()
    {
        $this->list = new ObjectList('stdClass');

        $this->object1 = new stdClass();
        $this->object2 = new stdClass();
        $this->object3 = new stdClass();

        $this->array = array(
            $this->object1,
            $this->object2,
            $this->object3
        );

        $this->list
                ->add($this->object1)
                ->add($this->object2)
                ->add($this->object3);
    }

    public function tearDown()
    {
        $this->list->clear();
    }

    public function testConstruct()
    {
        $newList = new ObjectList('stdClass');

        $this->assertEquals('stdClass', $newList->getType());
        $this->assertTrue($newList->isEmpty());
    }

    public function testConstructFromArray()
    {
        $newList = new ObjectList('stdClass', $this->array);

        $this->assertEquals($this->array, $newList->toArray());
    }

    public function testGetType()
    {
        $this->assertEquals('stdClass', $this->list->getType());
    }

    public function testSetType()
    {
        $this->list->clear();

        $this->list->setType('Exception');

        $this->assertEquals('Exception', $this->list->getType());
    }

    public function testSetTypeInvalidArgumentException()
    {
        $this->setExpectedException('InvalidArgumentException');

        $this->list
                ->clear()
                ->setType('AbraCadabraClass');
    }

    public function testSetTypeUnexpectedValueException()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->list->setType('Exception');
    }

    public function testGetRange()
    {
        $this->assertEquals(array($this->object2, $this->object3),
                            $this->list->getRange(1, 2)->toArray());
        $this->assertEquals(array($this->object1, $this->object2),
                            $this->list->getRange(0, 1)->toArray());
        $this->assertEquals(array($this->object1, $this->object2, $this->object3),
                            $this->list->getRange(0, 2)->toArray());

        $this->assertEquals(array($this->object1, $this->object2, $this->object3),
                            $this->list->getRange(0, 18)->toArray());
    }

    public function testAdd()
    {
        $this->list
                ->clear()
                ->add($this->object1)
                ->add($this->object2)
                ->add($this->object3);

        $this->assertEquals($this->object1, $this->list->getAt(0));
        $this->assertEquals($this->object2, $this->list->getAt(1));
        $this->assertEquals($this->object3, $this->list->getAt(2));

        $this->setExpectedException('UnexpectedValueException');

        $this->list->add(new Exception());
    }

    public function testAddFirst()
    {
        $this->list
                ->clear()
                ->addFirst($this->object2)
                ->addFirst($this->object1);

        $this->assertEquals(array($this->object1, $this->object2),
                            $this->list->toArray());
    }

    public function testAddLast()
    {
        $this->list
                ->clear()
                ->addLast($this->object1)
                ->addLast($this->object2);

        $this->assertEquals(array($this->object1, $this->object2),
                            $this->list->toArray());
    }

    public function testInsert()
    {
        $object0  = new stdClass();
        $object01 = new stdClass();
        $object02 = new stdClass();
        $object4  = new stdClass();

        $this->list->insert(0, $object0);

        $this->assertEquals(array($object0, $this->object1, $this->object2, $this->object3),
                            $this->list->toArray());

        $this->list->insert(1, $object01);

        $this->assertEquals(array($object0, $object01, $this->object1, $this->object2, $this->object3),
                            $this->list->toArray());

        $this->list->insert(5, $object4);

        $this->assertEquals(array($object0, $object01, $this->object1, $this->object2, $this->object3, $object4),
                            $this->list->toArray());
    }

    public function testSet()
    {
        $object = new stdClass();

        $this->list->set(0, $object);

        $this->assertEquals(array($object, $this->object2, $this->object3),
                            $this->list->toArray());
    }

    public function testIndexOf()
    {
        $this->assertEquals(0, $this->list->indexOf($this->object1));
        $this->assertEquals(1, $this->list->indexOf($this->object2));
        $this->assertEquals(2, $this->list->indexOf($this->object3));

        $this->setExpectedException('UnexpectedValueException');

        $this->list->indexOf(new stdClass());
    }

    public function testIndexOfAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->list->indexOf(new Exception());
    }

    public function testLastIndexOf()
    {
        $this->list->insert(3, $this->object1);

        $this->assertEquals(3, $this->list->lastIndexOf($this->object1));
        $this->assertEquals(1, $this->list->lastIndexOf($this->object2));
        $this->assertEquals(2, $this->list->lastIndexOf($this->object3));

        $this->setExpectedException('UnexpectedValueException');

        $this->list->lastIndexOf(new stdClass());
    }

    public function testLastIndexOfAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->list->lastIndexOf(new Exception());
    }

    public function testContains()
    {
        $this->assertTrue($this->list->contains($this->object1));
        $this->assertTrue($this->list->contains($this->object2));
        $this->assertTrue($this->list->contains($this->object3));

        $this->assertFalse($this->list->contains(new stdClass()));

        $this->setExpectedException('UnexpectedValueException');

        $this->list->contains(new Exception());
    }

    public function testRemove()
    {
        $this->list->remove($this->object2);

        $this->assertEquals(array($this->object1, $this->object3),
                            $this->list->toArray());

        $this->setExpectedException('UnexpectedValueException');

        $this->list->remove(new Exception());
    }

}