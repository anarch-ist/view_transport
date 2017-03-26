<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface ITransportCompany
{
    function selectAllCompanies();
    
    function selectCompanyById($id);
    
    function insertCompany();
}