package com.example.services.impl;

import com.example.models.Company;
import com.example.repositories.CompanyRepository;
import com.example.services.CompanyService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void addCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public Company updateCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    private void validator(String companyName, String locatedCountry) throws IOException {
        if (companyName.length()>2 && locatedCountry.length()>2) {
            for (Character i : companyName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("Numbers cannot be inserted in the company name");
                }
            }
            for (Character i : locatedCountry.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("Numbers cannot be inserted in the country name");
                }
            }
        }else {
            throw new IOException("The name of the company or country must contain at least 2 letters");
        }
    }
}
