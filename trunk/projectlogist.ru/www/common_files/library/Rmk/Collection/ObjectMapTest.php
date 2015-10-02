<?php
/**
 * Автотест карты объектов общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\ObjectMap,
    stdClass,
    Exception,
    PHPUnit_Framework_TestCase;

class ObjectMapTest extends PHPUnit_Framework_TestCase
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
     * @var ObjectMap
     */
    protected $map;

    public function setUp()
    {
        $this->map = new ObjectMap('stdClass');

        $this->object1 = new stdClass();
        $this->object2 = new stdClass();
        $this->object3 = new stdClass();

        $this->array = array(
            'k1' => $this->object1,
            'k2' => $this->object2,
            'k3' => $this->object3
        );

        $this->map
                ->set('k1', $this->object1)
                ->set('k2', $this->object2)
                ->set('k3', $this->object3);
    }

    public function tearDown()
    {
        $this->map->clear();
    }

    public function testConstruct()
    {
        $newMap = new ObjectMap('stdClass');

        $this->assertEquals('stdClass', $newMap->getType());
        $this->assertTrue($newMap->isEmpty());
    }

    public function testConstructFromArray()
    {
        $newMap = new ObjectMap('stdClass', $this->array);

        $this->assertEquals($this->array, $newMap->toArray());
    }

    public function testGetType()
    {
        $this->assertEquals('stdClass', $this->map->getType());
    }

    public function testSetType()
    {
        $this->map->clear();

        $this->map->setType('Exception');

        $this->assertEquals('Exception', $this->map->getType());
    }

    public function testSetTypeInvalidArgumentException()
    {
        $this->setExpectedException('InvalidArgumentException');

        $this->map
                ->clear()
                ->setType('AbraCadabraClass');
    }

    public function testSetTypeUnexpectedValueException()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->map->setType('Exception');
    }

    public function testAdd()
    {
        $this->map
                ->clear()
                ->add($this->object1)
                ->add($this->object2)
                ->add($this->object3);

        $this->assertEquals($this->object1, $this->map->getAt(0));
        $this->assertEquals($this->object2, $this->map->getAt(1));
        $this->assertEquals($this->object3, $this->map->getAt(2));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->add(new Exception());
    }

    public function testKeyOf()
    {
        $this->assertEquals('k1', $this->map->keyOf($this->object1));
        $this->assertEquals('k2', $this->map->keyOf($this->object2));
        $this->assertEquals('k3', $this->map->keyOf($this->object3));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->keyOf(new stdClass());
    }

    public function testKeyOfAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->map->keyOf(new Exception());
    }

    public function testLastKeyOf()
    {
        $this->map->set('k4', $this->object1);

        $this->assertEquals('k4', $this->map->lastKeyOf($this->object1));
        $this->assertEquals('k2', $this->map->lastKeyOf($this->object2));
        $this->assertEquals('k3', $this->map->lastKeyOf($this->object3));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->lastKeyOf(new stdClass());
    }

    public function testLastKeyOfAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->map->keyOf(new Exception());
    }

    public function testContains()
    {
        $this->assertTrue($this->map->contains($this->object1));
        $this->assertTrue($this->map->contains($this->object2));
        $this->assertTrue($this->map->contains($this->object3));

        $this->assertFalse($this->map->contains(new stdClass()));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->contains(new Exception());
    }

    public function testRemove()
    {
        $this->map
                ->remove($this->object1)
                ->remove($this->object2)
                ->remove($this->object3);

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->remove(new Exception());
    }

}