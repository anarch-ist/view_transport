<?php
/**
 * Автотест хешированной карты объектов общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\ObjectHashSet,
    stdClass,
    Exception,
    PHPUnit_Framework_TestCase;

class ObjectHashSetTest extends PHPUnit_Framework_TestCase
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
     * Тестовый ключ.
     *
     * @var mixed
     */
    protected $key1;

    /**
     * Тестовый ключ.
     *
     * @var mixed
     */
    protected $key2;

    /**
     * Тестовый ключ.
     *
     * @var mixed
     */
    protected $key3;

    /**
     * Тестовый массив.
     *
     * @var array
     */
    protected $array;

    /**
     * Карта общего назначения.
     *
     * @var ObjectHashSet
     */
    protected $set;

    public function setUp()
    {
        $this->set = new ObjectHashSet('stdClass');

        $this->object1 = new stdClass();
        $this->object2 = new stdClass();
        $this->object3 = new stdClass();

        $this->key1 = spl_object_hash($this->object1);
        $this->key2 = spl_object_hash($this->object2);
        $this->key3 = spl_object_hash($this->object3);

        $this->array = array(
            $this->key1 => $this->object1,
            $this->key2 => $this->object2,
            $this->key3 => $this->object3
        );

        $this->set
                ->set($this->key1, $this->object1)
                ->set($this->key2, $this->object2)
                ->set($this->key3, $this->object3);
    }

    public function tearDown()
    {
        $this->set->clear();
    }

    public function testConstruct()
    {
        $newSet = new ObjectHashSet('stdClass');

        $this->assertEquals('stdClass', $newSet->getType());
        $this->assertTrue($newSet->isEmpty());

        $newSetFromArray = new ObjectHashSet('stdClass', $this->array);
        $this->assertEquals($this->array, $newSetFromArray->toArray());
    }

    public function testGetType()
    {
        $this->assertEquals('stdClass', $this->set->getType());
    }

    public function testSetType()
    {
        $this->set->clear();

        $this->set->setType('Exception');

        $this->assertEquals('Exception', $this->set->getType());
    }

    public function testSetTypeInvalidArgumentException()
    {
        $this->setExpectedException('InvalidArgumentException');

        $this->set
                ->clear()
                ->setType('AbraCadabraClass');
    }

    public function testSetTypeUnexpectedValueException()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->set->setType('Exception');
    }

    public function testAdd()
    {
        $this->set
                ->clear()
                ->add($this->object1)
                ->add($this->object2)
                ->add($this->object3);

        $this->assertEquals($this->object1, $this->set->getAt(0));
        $this->assertEquals($this->object2, $this->set->getAt(1));
        $this->assertEquals($this->object3, $this->set->getAt(2));

        $this->setExpectedException('UnexpectedValueException');

        $this->set->add(new Exception());
    }

    public function testSet()
    {
        $this->set
                ->set($this->key1, $this->object1)
                ->set($this->key2, $this->object2)
                ->set($this->key3, $this->object3);

        $this->setExpectedException('UnexpectedValueException');

        $this->set->set('object1', $this->object1);
    }

    public function testKeyOf()
    {
        $this->assertEquals($this->key1, $this->set->keyOf($this->object1));
        $this->assertEquals($this->key2, $this->set->keyOf($this->object2));
        $this->assertEquals($this->key3, $this->set->keyOf($this->object3));

        $this->setExpectedException('UnexpectedValueException');

        $this->set->keyOf(new stdClass());
    }

    public function testKeyOfAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $this->set->keyOf(new Exception());
    }

    public function testContains()
    {
        $this->assertTrue($this->set->contains($this->object1));
        $this->assertTrue($this->set->contains($this->object2));
        $this->assertTrue($this->set->contains($this->object3));

        $this->assertFalse($this->set->contains(new stdClass()));

        $this->setExpectedException('UnexpectedValueException');

        $this->set->contains(new Exception());
    }

    public function testRemove()
    {
        $this->set
                ->remove($this->object1)
                ->remove($this->object2)
                ->remove($this->object3);

        $this->assertTrue($this->set->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->set->remove(new Exception());
    }

}