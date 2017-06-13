<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface ITransportCompany
{
    function selectAllCompanies();
    
    function selectCompanyById($id);
    
    function insertCompany($transportCompanyInfo);

    function selectByRange($start = 0, $count = 20);

    function selectLastInsertedId();

    function pseudoRemoveCompany($userID);

    function updateCompany(TransportCompanyData $newCompany, $id);
}